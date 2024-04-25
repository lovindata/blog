package helpers

import org.mockito.MockitoSugar
import org.mockito.cats.MockitoCats
import org.mockito.matchers.MacroBasedMatchers
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

trait FreeSpec extends AnyFreeSpec with Matchers with MockitoSugar with MacroBasedMatchers with MockitoCats {
  def noop[A]: A = null.asInstanceOf[A]
}
