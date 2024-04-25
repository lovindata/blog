package helpers

import cats.effect.IO
import cats.effect.Resource
import cats.implicits._
import doobie.KleisliInterpreter
import doobie.util.log.LogHandler
import doobie.util.transactor.Strategy
import doobie.util.transactor.Transactor

object TestDoobieUtils {
  val noopTransactor = Transactor(
    (),
    (_: Unit) => Resource.pure[IO, java.sql.Connection](null),
    KleisliInterpreter[IO](LogHandler.noop).ConnectionInterpreter,
    Strategy.void
  )
}
