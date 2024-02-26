package com.ilovedatajjia
package config

import cats.effect.IO
import cats.implicits._
import com.comcast.ip4s._
import features.guest.GuestCtrl
import features.pet.PetCtrl
import features.text.TextCtrl
import org.http4s.ember.server.EmberServerBuilder
import shared.BackendException.ServerInternalErrorException
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter

object BackendServerConf {
  def start: IO[Unit] = for {
    port <- IO.fromOption(Port.fromInt(EnvLoaderConf.backendPort))(
              ServerInternalErrorException(s"Not processable port number ${EnvLoaderConf.backendPort}."))
    _    <- EmberServerBuilder
              .default[IO]
              .withHost(ipv4"0.0.0.0")        // Accept connections from any available network interface
              .withPort(port)                 // On port 8080
              .withHttpApp(allRts.orNotFound) // Link all routes to the backend server
              .build
              .use(_ => IO.never)
              .start
              .void
  } yield ()

  private val docsEpt = // Merge all endpoints as a fully usable OpenAPI doc
    SwaggerInterpreter()
      .fromEndpoints[IO](TextCtrl.endpoints ++ GuestCtrl.endpoints ++ PetCtrl.endpoints, "Backend", "1.0")
  private val allRts  = // Serve the OpenAPI doc & all the other routes
    Http4sServerInterpreter[IO]().toRoutes(docsEpt) <+> TextCtrl.routes <+> GuestCtrl.routes <+> PetCtrl.routes
}
