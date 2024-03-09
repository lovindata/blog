package com.lovindata

import cats.effect._
import config.BackendServerConf

object Main extends IOApp.Simple {
  override def run: IO[Unit] = BackendServerConf.start >> IO.never // == non-terminating
}
