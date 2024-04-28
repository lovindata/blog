import cats.effect.IO
import cats.effect.IOApp
import confs.ApiConf
import confs.DbConf

object Main extends IOApp.Simple {
  override def run: IO[Unit] = DbConf.impl.setup >> ApiConf.impl.setup >> IO.never
}
