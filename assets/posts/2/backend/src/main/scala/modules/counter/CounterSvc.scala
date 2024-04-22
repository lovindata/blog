package modules.counter

import cats.effect.IO
import confs.DbConf

final case class CounterSvc()(implicit dbConf: DbConf, counterRep: CounterRep) {
  def getOrCreate: IO[Long] = dbConf.run(counterRep.getOrCreate.map(_.count))

  def addOne: IO[Long] = dbConf.run(for {
    counter       <- counterRep.getOrCreate
    counterUpdated = counter.addOne
    _             <- counterRep.update(counterUpdated)
    count          = counterUpdated.count
  } yield count)
}

object CounterSvc { implicit val impl: CounterSvc = CounterSvc() }