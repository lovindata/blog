package modules

import helpers.FreeSpec
import modules.counter.CounterMod
import scala.util.Random

final case class CounterModTest() extends FreeSpec {
  "addOne" - {
    Vector(("correct for negative count", Long.MinValue),
           ("correct for neutral count", 0L),
           ("correct for positive count", Long.MaxValue)).foreach { case (ut, count) =>
      ut in {
        val input  = spy(CounterMod(noop, count))
        val output = input.addOne
        output.count shouldBe (input.count + 1)
      }
    }
  }
}
