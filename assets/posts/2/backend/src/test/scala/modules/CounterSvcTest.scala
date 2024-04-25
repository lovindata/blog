package modules

import cats.effect.IO
import cats.effect.Resource
import cats.effect.unsafe.implicits.global
import cats.implicits._
import confs.DbConf
import doobie.ConnectionIO
import doobie.KleisliInterpreter
import doobie.implicits._
import helpers.FreeSpec
import helpers.TestDoobieUtils.noopTransactor
import modules.counter.CounterMod
import modules.counter.CounterRep
import modules.counter.CounterSvc
import scala.util.Random

final case class CounterSvcTest() extends FreeSpec {
  private val dbConf: DbConf         = mock[DbConf]
  private val counterRep: CounterRep = mock[CounterRep]
  private val counterSvc             = CounterSvc()(dbConf, counterRep)

  "addOne" - {
    "successful for any count" in {
      val input  = spy(CounterMod(noop, Random.nextLong()))
      whenF(counterRep.getOrCreate).thenReturn(input)
      whenF(counterRep.update(*)).thenReturn(noop)
      when(dbConf.run(*[ConnectionIO[Any]])).thenAnswer((sqls: ConnectionIO[Any]) => sqls.transact[IO](noopTransactor))
      val output = counterSvc.addOne.unsafeRunSync()
      output shouldBe (input.count + 1)
    }
  }
}
