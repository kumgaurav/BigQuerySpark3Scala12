package com.kumargaurav
/**
 * @author Kumar Gaurav
 * @Date Create May 1, 2021
 */

import org.slf4j.LoggerFactory
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.current_date
import org.apache.spark.sql.SaveMode
import scala.util.control.Exception.Catch
import com.kumargaurav.utils.MiscUtil._

object WriteBQTableWithPartitionDemo {
  val log = LoggerFactory.getLogger(this.getClass)
  val appName = this.getClass.getName.init
  val DATE_FORMAT = "YYYYMMDD"
  def main(args: Array[String]): Unit = {
    val startTime = System.currentTimeMillis()
    val gcsBucket = "my-temp-gcs-bucket"
    val tableName = "project_id"+"."+"datasetname"+"."+"tableName" //replace this with your project name and dataset name
    val sparkConf = new SparkConf().setAppName(appName).setMaster("local[*]")
    implicit val spark = SparkSession.builder().config(sparkConf).getOrCreate()
    import com.google.cloud.spark.bigquery._
    try{
      val df = spark.read.bigquery("bigquery-public-data.samples.shakespeare")
      val df_withdate = df.withColumn("record_ingestion_date",current_date())
      df_withdate.write.format("bigquery")
      .option("temporaryGcsBucket", gcsBucket)
      .option("partitionField", "record_ingestion_date")
      .option("partitionType", "DAY")
      .option("partitionExpirationMs", "15552000000")//180 days
      .option("clusterFields", "word")
      .mode(SaveMode.Overwrite)
      .save(tableName)
    }catch{
      case e:Exception =>
        log.error(e.getMessage,e)
        safeExit(1, startTime)
    }
    safeExit(0, startTime)
  }
  
  def safeExit(exitCode: Int, startTime:Long)(implicit spark:SparkSession): Unit = {
    if(exitCode == 0){
      val duration = convertMilSecondsToHMmSs(System.currentTimeMillis() - startTime)
      log.info(s"Process finish $appName in $duration")
    }
    spark.close()
    sys.exit(exitCode)
  }
}