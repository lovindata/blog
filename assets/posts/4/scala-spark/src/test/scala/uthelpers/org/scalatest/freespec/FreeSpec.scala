package uthelpers.org.scalatest.freespec

import org.apache.spark.sql.SparkSession
import org.mockito.MockitoSugar
import org.mockito.matchers.MacroBasedMatchers
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

trait FreeSpec extends AnyFreeSpec with Matchers with MockitoSugar with MacroBasedMatchers {
  val spark = SparkSession.builder().appName("UT").master("local[*]").getOrCreate()
  spark.sparkContext.setLogLevel("WARN")
}
