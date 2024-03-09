package com.lovindata
package features.pet

import cats.effect.IO
import features.pet.dto.PetDto
import features.shared.Serializers._ // ✨ Auto Derivation Magic + 2 implicits imported in scope! (== Make Scala sealed traits "JSON ↔ Scala" convertible)
import org.http4s.HttpRoutes
import shared.BackendException.BadRequestException
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.tapir.server.http4s.Http4sServerInterpreter

object PetCtrl {
  def endpoints: List[AnyEndpoint] = List(petContestEpt)
  def routes: HttpRoutes[IO]       = petContestRts

  private val petContestEpt = endpoint
    .summary("Pet contest")
    .post
    .in("pets" / "contest")
    .in(jsonBody[Vector[PetDto]])                                                   // ✨ Auto Derivation Magic applied! (A sealed trait this time :O)
    .out(jsonBody[PetDto])                                                          // ✨ Auto Derivation Magic applied!
    .errorOut(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequestException])) // ✨ Auto Derivation Magic applied!
  private val petContestRts =
    Http4sServerInterpreter[IO]().toRoutes(petContestEpt.serverLogicRecoverErrors(dto => PetSvc.petContest(dto)))
}
