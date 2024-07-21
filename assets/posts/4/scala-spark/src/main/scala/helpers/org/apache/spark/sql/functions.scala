package helpers.org.apache.spark.sql

import org.apache.spark.sql.Column
import org.apache.spark.sql.functions.col

object functions {

  /**
   * Like [[org.apache.spark.sql.functions.col]] but consider the column as a raw name.
   */
  def col_(colName: String): Column = col(s"`${colName.replace("`", "``")}`")
}
