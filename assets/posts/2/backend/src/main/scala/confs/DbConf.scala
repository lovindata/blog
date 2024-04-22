package confs

import cats.effect.IO
import cats.effect.Resource
import doobie.ConnectionIO
import doobie.hikari.HikariTransactor
import doobie.implicits._
import doobie.util.ExecutionContexts
import org.flywaydb.core.Flyway

case class DbConf()(implicit envConf: EnvConf) {
  def setup: IO[Unit] = IO.blocking {
    Flyway.configure
      .dataSource(
        s"jdbc:postgresql://${envConf.postgresIp}:${envConf.postgresPort}/${envConf.postgresDb}?currentSchema=${envConf.postgresSchema}",
        envConf.postgresUser,
        envConf.postgresPassword
      )
      .group(true)
      .table("flyway")                 // ⚠️ "flyway" as migration table history (in 'currentSchema' see above)
      .locations("conf/DbConf/flyway") // ".sql" files migration resource path
      .failOnMissingLocations(true)
      .load
      .migrate                         // Auto create schema if not exists & Rollback raise exception if failed
  }

  def run[A](sqls: ConnectionIO[A]): IO[A] = transactor.use(sqls.transact[IO])

  private val transactor: Resource[IO, HikariTransactor[IO]] = for {
    ce <- ExecutionContexts.fixedThreadPool[IO](32)
    xa <- HikariTransactor.newHikariTransactor[IO](
            "org.postgresql.Driver",
            s"jdbc:postgresql://${envConf.postgresIp}:${envConf.postgresPort}/${envConf.postgresDb}?currentSchema=${envConf.postgresSchema}",
            envConf.postgresUser,
            envConf.postgresPassword,
            ce
          )
  } yield xa
}

object DbConf { implicit val impl: DbConf = DbConf() }
