package com.ilovedatajjia
package features.text

import cats.effect.IO
import org.http4s.HttpRoutes
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.tapir.server.http4s.Http4sServerInterpreter

object TextCtrl {
  def endpoints: List[AnyEndpoint] = List(countCharactersEpt)
  def routes: HttpRoutes[IO]       = countCharactersRts

  private val countCharactersEpt = endpoint // The endpoint and it is used to generate the OpenAPI doc
    .summary("Count characters")
    .get
    .in("count-characters" / query[String]("text"))
    .out(jsonBody[Int])
  private val countCharactersRts =          // It converts the endpoint to actual http4s route :O
    Http4sServerInterpreter[IO]().toRoutes(countCharactersEpt.serverLogicSuccess(text => TextSvc.countCharacters(text)))
}
