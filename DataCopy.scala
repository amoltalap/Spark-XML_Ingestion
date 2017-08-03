import org.apache.spark.sql.SparkSession

/**
  * Created by atalap on 7/27/2017.
  */
object DataCopy {

  def copyData(dataTableName:String,spark:SparkSession){

    var _ORACLEserver : String = "jdbc:mysql://localhost:3306/oracle_db"
    var _username : String = "spark"
    var _password : String = "tomtom"

    val jdbcDF = spark.read
      .format("jdbc")
      .option("url" ,_ORACLEserver)
      .option("driver", "com.mysql.jdbc.Driver")
      .option("dbtable" , dataTableName)
      .option("user" , _username)
      .option("password" , _password)
      .load()

    jdbcDF.createGlobalTempView("tmp")
    val df = spark.sqlContext.sql("SELECT * FROM tmp")
    df.collect.foreach(println)
  }

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local")
      .appName("JDBC_DataIngestion")
      //.enableHiveSupport()
      .getOrCreate()

    val dataTableName = args(0).toString
    copyData(dataTableName, spark) //"data_tbl2"
  }
}
