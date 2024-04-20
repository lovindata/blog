package com.lovindata
package conf

import cats.effect.IO
import cats.effect.Resource
import cats.implicits.catsSyntaxApplicativeId
import doobie.ConnectionIO
import doobie.hikari.HikariTransactor
import doobie.implicits._
import doobie.util.ExecutionContexts
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.output.MigrateResult
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.syntax.LoggerInterpolator
import scala.jdk.CollectionConverters.ListHasAsScala

case class DbConf()(implicit envConf: EnvConf, logger: Logger[IO] = Slf4jLogger.getLogger) {
  def setup: IO[Unit] = {
    def migrate: IO[MigrateResult]                        =
      info"Starting ${envConf.postgresDb} database ${envConf.postgresSchema} schema migration ⏳..." >>
        IO.blocking {
          Flyway.configure
            .dataSource(
              s"jdbc:postgresql://${envConf.postgresIp}:${envConf.postgresPort}/${envConf.postgresDb}?currentSchema=${envConf.postgresSchema}",
              envConf.postgresUser,
              envConf.postgresPassword
            )
            .group(true)
            .table("flyway")                 // ⚠️ "flyway" as migration table history
            .locations("conf/DbConf/flyway") // ".sql" files migration resource path
            .failOnMissingLocations(true)
            .load
            .migrate                         // Auto create schema if not exists & Rollback raise exception if failed
        }
    def logResult(migrateResult: MigrateResult): IO[Unit] = for {
      migrations <- migrateResult.migrations.asScala.map(_.filepath).toVector.pure[IO] // Detected migrations
      _          <- if (migrations.isEmpty) info"No migration processed."
                    else info"""${migrations.length} migration(s) processed:\n
                    ${migrations.mkString("\t-", "\n\t-", "")}
                    """
    } yield ()

    for {
      result <- migrate
      _      <- logResult(result)
      _      <- run(sql"SELECT 1;")
    } yield ()
  }

  def run[A](sqls: ConnectionIO[A]): IO[A] = transactor.use(sqls.transact[IO])

  private val transactor: Resource[IO, HikariTransactor[IO]] = for {
    ce <- ExecutionContexts.fixedThreadPool[IO](32) // our connect EC
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
