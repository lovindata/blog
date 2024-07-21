import org.apache.spark.sql.SparkSession

object Main extends App {
  // println("Hello world!")
  val spark = SparkSession.builder().appName("DEV").master("local[2]").getOrCreate()
  spark.emptyDataFrame.show(false)
}
