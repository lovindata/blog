package confs

import cats.effect.IO
import cats.implicits._
import com.comcast.ip4s.IpLiteralSyntax
import com.comcast.ip4s.Port
import modules.counter.CounterCtrl
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.middleware.{Logger => LoggerMiddleware}
import org.http4s.server.middleware.CORS
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.syntax.LoggerInterpolator
import sttp.tapir.emptyInput
import sttp.tapir.files.FilesOptions
import sttp.tapir.files.staticFilesGetServerEndpoint
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter

final case class ApiConf()(implicit
    envConf: EnvConf,
    counterCtrl: CounterCtrl,
    logger: Logger[IO] = Slf4jLogger.getLogger) {
  def setup: IO[Unit] = for {
    port      <-
      IO.fromOption(Port.fromInt(envConf.port))(new RuntimeException(s"Not processable port number ${envConf.port}."))
    corsPolicy = CORS.policy.withAllowOriginHostCi(_ =>
                   envConf.devMode) // Essential for local development setup with an SPA running on a separate port
    _         <- EmberServerBuilder
                   .default[IO]
                   .withHost(ipv4"0.0.0.0")                    // Accept connections from any available network interface
                   .withPort(port)                             // On a given port
                   .withHttpApp(corsPolicy(allRts).orNotFound) // Link all routes to the backend server
                   .build
                   .use(_ => IO.never)
                   .start
                   .void
  } yield ()

  private val frontendServerLogic =
    staticFilesGetServerEndpoint[IO](emptyInput)(
      "../frontend/dist",                                  // Serve frontend static files from '../frontend/dist' at '/'
      FilesOptions.default.defaultFile(List("index.html")) // Serve '../frontend/dist/index.html' if not found
    )
  private val frontendEpt         =
    frontendServerLogic.endpoint.summary("Frontend served from '../frontend/dist' on the file system")
  private val frontendRts         = Http4sServerInterpreter[IO]().toRoutes(frontendServerLogic)

  private val docsEpt =
    SwaggerInterpreter().fromEndpoints[IO](
      counterCtrl.endpoints :+ // Here's where the new endpoint definition is added!
        frontendEpt,
      "Backend – TARP Stack ⛺",
      "1.0")
  private val allRts  = {
    val loggerMiddleware =
      LoggerMiddleware.httpRoutes(                 // To log incoming requests or outgoing responses from the server
        logHeaders = true,
        logBody = true,
        redactHeadersWhen = _ => !envConf.devMode, // Display header values exclusively during development mode
        logAction = Some((msg: String) => info"$msg")
      )(_)
    Http4sServerInterpreter[IO]().toRoutes(docsEpt) <+>
      loggerMiddleware(counterCtrl.routes) <+> // Here's where the new endpoint logic is added!
      frontendRts // Endpoints are resolved by order, so it's crucial to place frontend logic last! Otherwise, it will always serve 'index.html'!
  }
}

object ApiConf { implicit val impl: ApiConf = ApiConf() }
