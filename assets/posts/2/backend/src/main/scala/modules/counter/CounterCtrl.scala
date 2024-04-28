package modules.counter

import cats.effect.IO
import cats.implicits._
import org.http4s.HttpRoutes
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.tapir.server.http4s.Http4sServerInterpreter

final case class CounterCtrl()(implicit counterSvc: CounterSvc) {
  def endpoints: List[AnyEndpoint] = List(getEpt, addOneEpt)
  def routes: HttpRoutes[IO]       = getRts <+> addOneRts

  private val getEpt = endpoint.summary("Get counter").get.in("api" / "counter").out(jsonBody[Long])
  private val getRts = Http4sServerInterpreter[IO]().toRoutes(getEpt.serverLogicSuccess(_ => counterSvc.getOrCreate))

  private val addOneEpt =
    endpoint.summary("Add one to counter").post.in("api" / "counter" / "add-one").out(jsonBody[Long])
  private val addOneRts = Http4sServerInterpreter[IO]().toRoutes(addOneEpt.serverLogicSuccess(_ => counterSvc.addOne))
}

object CounterCtrl { implicit val impl: CounterCtrl = CounterCtrl() }
