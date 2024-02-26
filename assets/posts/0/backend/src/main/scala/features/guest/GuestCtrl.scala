package com.ilovedatajjia
package features.guest

import cats.effect.IO
import cats.implicits._
import features.guest.dto.GuestDto
import features.shared.Serializers._ // ✨ Auto Derivation Magic imported in scope! (== Make Scala case classes "JSON ↔ Scala" convertible)
import org.http4s.HttpRoutes
import shared.BackendException.BadRequestException
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.tapir.server.http4s.Http4sServerInterpreter

object GuestCtrl {
  def endpoints: List[AnyEndpoint] = List(letEnterAdultGuestEpt, listGuestsEpt)
  def routes: HttpRoutes[IO]       = letEnterAdultGuestRts <+> listGuestsRts

  private val letEnterAdultGuestEpt = endpoint
    .summary("Let enter adult guest")
    .post
    .in("guests")
    .in(jsonBody[GuestDto])                 // ✨ Auto Derivation Magic applied! (Not just simple type but case class this time)
    .out(jsonBody[GuestMod])                // ✨ Auto Derivation Magic applied!
    .errorOut(
      statusCode(StatusCode.BadRequest)
        .and(jsonBody[BadRequestException]) // This endpoint can throw errors + ✨ Auto Derivation Magic applied!
    )
  private val letEnterAdultGuestRts = Http4sServerInterpreter[IO]().toRoutes(
    letEnterAdultGuestEpt
      .serverLogicRecoverErrors( // == recover from "BadRequestException" exceptions raised + Encode as JSON and return them
        dto => GuestSvc.letEnterAdultGuest(dto)))

  private val listGuestsEpt = endpoint
    .summary("List guests")
    .get
    .in("guests")
    .out(
      jsonBody[
        Vector[
          GuestMod // /!\ Vector of Scala case class derivable is also derivable == ✨ Auto Derivation Magic applied!
        ]
      ]
    )
  private val listGuestsRts =
    Http4sServerInterpreter[IO]().toRoutes(listGuestsEpt.serverLogicSuccess(_ => GuestSvc.listGuests()))
}
