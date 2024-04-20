package com.lovindata

import cats.effect.IO
import cats.effect.IOApp
import conf.ApiConf
import conf.DbConf

object Main extends IOApp.Simple {
  override def run: IO[Unit] = DbConf.impl.setup >> ApiConf.impl.setup >> IO.never
}
