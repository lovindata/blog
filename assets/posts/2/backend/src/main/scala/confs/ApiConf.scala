package confs

import cats.data.Kleisli
import cats.effect.IO
import cats.implicits._
import com.comcast.ip4s.IpLiteralSyntax
import com.comcast.ip4s.Port
import modules.counter.CounterCtrl
import org.http4s.HttpApp
import org.http4s.HttpRoutes
import org.http4s.Request
import org.http4s.Response
import org.http4s.Status
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.middleware.{Logger => LoggerMiddleware}
import org.http4s.server.middleware.CORS
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.syntax.LoggerInterpolator
import scala.io.Source
import sttp.tapir.emptyInput
import sttp.tapir.files.FilesOptions
import sttp.tapir.files.staticFilesGetServerEndpoint
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter

case class ApiConf()(implicit envConf: EnvConf, counterCtrl: CounterCtrl, logger: Logger[IO] = Slf4jLogger.getLogger) {
  def setup: IO[Unit] = for {
    port      <- IO.fromOption(Port.fromInt(envConf.backendPort))(
                   new Throwable(s"Not processable port number ${envConf.backendPort}."))
    corsPolicy = CORS.policy.withAllowOriginHostCi(_ => envConf.backendDevMode)
    _         <- EmberServerBuilder
                   .default[IO]
                   .withHost(ipv4"0.0.0.0")                    // Accept connections from any available network interface
                   .withPort(port)                             // On port 8080
                   .withHttpApp(corsPolicy(allRts).orNotFound) // Link all routes to the backend server
                   .build
                   .use(_ => IO.never)
                   .start
                   .void
  } yield ()

  private val frontendServerLogic =
    staticFilesGetServerEndpoint[IO](emptyInput)("../frontend/dist",
                                                 FilesOptions.default.defaultFile(List("index.html")))
  private val frontendEpt         =
    frontendServerLogic.endpoint.summary("Frontend served from ../frontend/dist on the file system")
  private val frontendRts         = Http4sServerInterpreter[IO]().toRoutes(frontendServerLogic)

  private val docsEpt =
    SwaggerInterpreter().fromEndpoints[IO](counterCtrl.endpoints :+ frontendEpt, "Backend – TARP Stack ⛺", "1.0")
  private val allRts  = {
    val loggerMiddleware = LoggerMiddleware.httpRoutes(
      logHeaders = true,
      logBody = true,
      redactHeadersWhen = _ => !envConf.backendDevMode,
      logAction = Some((msg: String) => info"$msg")
    )(_)
    Http4sServerInterpreter[IO]().toRoutes(docsEpt) <+> loggerMiddleware(counterCtrl.routes) <+> frontendRts
  }
}

object ApiConf { implicit val impl: ApiConf = ApiConf() }
