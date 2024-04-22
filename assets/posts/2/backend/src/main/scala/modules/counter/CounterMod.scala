package modules.counter

import monocle.syntax.all._

case class CounterMod(id: Long, count: Long) {
  def addOne: CounterMod = this.focus(_.count).modify(_ + 1)
}
