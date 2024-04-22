package modules.counter

import cats.effect.IO
import cats.implicits._
import confs.DbConf
import doobie.ConnectionIO
import doobie.implicits._
import modules.counter.CounterMod

final case class CounterRep()(implicit dbConf: DbConf) {
  def getOrCreate: ConnectionIO[CounterMod] = {
    def createIfNotFound(counterFound: Option[CounterMod]): ConnectionIO[CounterMod] = counterFound match {
      case Some(counter) => counter.pure[ConnectionIO]
      case None          =>
        sql"""|INSERT INTO counter (count)
              |VALUES (0);""".stripMargin.update.withUniqueGeneratedKeys[Long]("id") >>=
          (id => sql"""|SELECT *
                       |FROM counter
                       |WHERE id = $id;""".stripMargin.query[CounterMod].unique)
    }

    for {
      counterFound <- sql"""|SELECT *
                            |FROM counter
                            |WHERE id = 1;""".stripMargin.query[CounterMod].option
      counter      <- createIfNotFound(counterFound)
    } yield counter
  }

  def update(counter: CounterMod): ConnectionIO[Unit] =
    sql"""|UPDATE counter
          |SET count = ${counter.count};""".stripMargin.update.run.void
}

object CounterRep { implicit val impl: CounterRep = CounterRep() }
