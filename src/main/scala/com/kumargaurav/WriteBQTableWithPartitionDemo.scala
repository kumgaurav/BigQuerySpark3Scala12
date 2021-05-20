package com.kumargaurav
/**
 * @author Kumar Gaurav
 * @Date Create May 1, 2021
 */

import org.slf4j.LoggerFactory
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.SaveMode
import scala.util.control.Exception.Catch
import com.kumargaurav.utils.MiscUtil._
import java.time.format.DateTimeFormatter
import java.util.Date
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId

object WriteBQTableWithPartitionDemo {
  val log = LoggerFactory.getLogger(this.getClass)
  val appName = this.getClass.getName.init
  val DATE_FORMAT = "yyyyMMdd"
  def main(args: Array[String]): Unit = {
    val startTime = System.currentTimeMillis()
    val gcsBucket = "ite-poc"
    val project_id = "itd-aia-datalake"
    val datasetname = "c360_dm"
    val tableName = "partion_date_poc"
    val tableNameForWrite = project_id+"."+datasetname+"."+tableName //replace this with your project name and dataset name
    val sparkConf = new SparkConf().setAppName(appName)
    .set("spark.jars.packages", "com.google.cloud.spark:spark-bigquery-with-dependencies_2.12:0.20.0")
    //.set("credentialsFile", "itd-aia-c360.json")
    //.setMaster("local[*]")
    implicit val spark = SparkSession.builder().config(sparkConf).getOrCreate()
    import com.google.cloud.spark.bigquery._
    try{
      val dt = new Date()
      val expirationWindowDate:Date = convertToDateViaInstant(LocalDate.now.minusDays(30))
      val dateFormat = new SimpleDateFormat(DATE_FORMAT)
      val dateStr = dateFormat.format(expirationWindowDate)
      log.info("dateStr -> "+dateStr)
      val df = spark.read.bigquery("bigquery-public-data.samples.shakespeare")
      val df_withdate = df.withColumn("record_ingestion_date",to_date(lit(dateStr),DATE_FORMAT))
      df_withdate.show(10)
      df_withdate.write.format("bigquery")
      .option("temporaryGcsBucket", gcsBucket)
      .option("createDisposition", "CREATE_IF_NEEDED") 
      .option("datePartition", dateStr) 
      .option("partitionField", "record_ingestion_date") 
      .option("partitionType", "DAY")
      .option("partitionExpirationMs", "15552000000")//180 days
      .option("clusteredFields", "word")
      .mode(SaveMode.Overwrite)
      .save(tableNameForWrite)
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
  
  private def convertToDateViaInstant(dateToConvert: LocalDate) = java.util.Date.from(dateToConvert.atStartOfDay.atZone(ZoneId.systemDefault).toInstant)
}