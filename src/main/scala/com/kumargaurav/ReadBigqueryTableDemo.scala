package com.kumargaurav

import org.slf4j.LoggerFactory
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object ReadBigqueryTableDemo {
  val log =  LoggerFactory.getLogger(this.getClass)
  val appName = this.getClass.getName.init
  def main(args: Array[String]): Unit = {
    log.info("Scala version -> "+scala.util.Properties.versionString)
    val sparkConf = new SparkConf().setMaster("local[*]")
    .setAppName(appName)
    .set("spark.jars.packages", "com.google.cloud.spark:spark-bigquery-with-dependencies_2.12:0.20.0")
    implicit val spark = SparkSession.builder().config(sparkConf).getOrCreate()
    import com.google.cloud.spark.bigquery._
    val df = spark.read.bigquery("bigquery-public-data.samples.shakespeare")
    df.show(2)
  }
}