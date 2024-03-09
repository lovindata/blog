package com.lovindata
package features.text

import cats.effect.IO
import cats.implicits._

object TextSvc {
  def countCharacters(text: String): IO[Int] = text.length.pure[IO]
}
