package com.kumargaurav
/**
 * @author Kumar Gaurav
 * @Date Create May 1, 2021
 */
import org.slf4j.LoggerFactory
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import com.google.cloud.spark.bigquery._
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.{DataType, DataTypes, DoubleType}
import org.apache.spark.sql.{Column, DataFrame, SparkSession}
import com.kumargaurav.utils.BulkOpUtils._
import org.apache.spark.sql.SaveMode

object ReadBigqueryTableDemo {
  val log =  LoggerFactory.getLogger(this.getClass)
  val appName = this.getClass.getName.init
  def main(args: Array[String]): Unit = {
    log.info("Scala version -> "+scala.util.Properties.versionString)
    val sparkConf = new SparkConf().setMaster("local[*]")
    .setAppName(appName)
    .set("spark.jars.packages", "com.google.cloud.spark:spark-bigquery-with-dependencies_2.12:0.20.0")
    .set("viewsEnabled","true")
    .set("spark.sql.legacy.timeParserPolicy", "LEGACY")
    implicit val spark: SparkSession = createSparkSession(appName, sparkConf)
    log.info("Spark version -> "+spark.sparkContext.version)
    import com.google.cloud.spark.bigquery._
    val df = spark.read.bigquery("bigquery-public-data.samples.shakespeare")
    //df.printSchema()
    //df.show(10)
    val schema_map = df.schema.map(s => (s.name, s.dataType.simpleString))
    //val timestampCols = schema_map.filter(_._2 == "timestamp").map(_._1)
    val timestampCols = List("createddate","lastmodifieddate","systemmodstamp")
    log.info("timestampCols -> "+timestampCols)
    val df_transformed = df.bulkColumnTransform(transformTimestampForSpark3, timestampCols)
    val df_transformed_1 = df_transformed.bulkColumnStringToTimeStamp(timestampCols)  
    df_transformed_1.printSchema()
    df_transformed_1.show(10)
    df_transformed_1.write.format("parquet").mode(SaveMode.Overwrite).save("/Users/gkumargaur/tmp/sap/")
    
    val rTest = spark.read.parquet("/Users/gkumargaur/tmp/sap/")
    rTest.printSchema()
    rTest.show()
    
  }
  
  
}