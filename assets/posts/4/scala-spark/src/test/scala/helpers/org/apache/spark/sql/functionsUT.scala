package helpers.org.apache.spark.sql

import helpers.org.apache.spark.sql.functions.col_
import org.apache.spark.sql.Row
import org.apache.spark.sql.functions.lit
import org.apache.spark.sql.types.NullType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType
import scala.util.Success
import scala.util.Try
import uthelpers.org.scalatest.freespec.FreeSpec

final case class functionsUT() extends FreeSpec {
  "col_" - {
    Vector(("""column with "." character in the name""", ".col.Name..With.Dots."),
           ("""column with "`" character in the name""", "`col`Name``With`Backticks`")).foreach { case (ut, colName) =>
      ut in {
        val input  = col_(colName)
        val schema = StructType(Seq(StructField(colName, NullType, true)))
        val df     = spark.createDataFrame(spark.sparkContext.emptyRDD[Row], schema)
        val output = Try(df.select(input))
        output.getClass() shouldBe Success.getClass()
      }
    }
  }
}
