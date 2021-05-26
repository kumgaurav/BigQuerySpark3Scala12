package com.kumargaurav.utils

import java.sql.Date

import org.apache.spark.sql.{Column, DataFrame, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{DataType, DataTypes, DoubleType}
import org.apache.spark.SparkConf
import org.apache.spark.sql.types.{TimestampType,StringType}
import org.apache.spark.sql.expressions.Window

object DateFormat extends Enumeration {
  val EPOCH = "yyyy-MM-dd'T'HH:mm:ss"
  val STD = "yyyy-MM-dd HH:mm:ss"
  val SHORT = "yyyy-MM-dd"
  val getAll = List(EPOCH, STD, SHORT)
}

object BulkOpUtils {
  
  private var _spark: SparkSession = _

  // enforce singleton
  def createSparkSession(programName:String, sparkConf :SparkConf) = {
    if (_spark == null) {
      _spark = SparkSession.builder()
        .appName(programName)
        .config(sparkConf)
        .getOrCreate()
    }
    _spark
  }

  def transformDateForWave(c: String): Column = {
    val formattedDates = DateFormat.getAll
      .map(fm => from_unixtime(unix_timestamp(col(c).cast(StringType), fm.toString), DateFormat.SHORT).cast(StringType)).toSeq

    coalesce(formattedDates:_*)
  }
  def transformDateForSpark3(c: String): Column = {
      when(col(c).isNotNull && col(c) < to_date(lit("1900-01-01"), "yyyy-MM-dd"), to_date(lit("1900-01-01"), "yyyy-MM-dd")).otherwise(col(c))
  }
    
    def transformTimestampForSpark3(c: String): Column = {
      when(col(c).isNotNull && col(c) < to_date(lit("1900-01-01'T'00:00:00"), DateFormat.EPOCH), to_timestamp(lit("1900-01-01'T'00:00:00"), DateFormat.EPOCH)).otherwise(date_format(col(c),DateFormat.EPOCH))
    }
    
    implicit class MyDfImp(df: DataFrame)(implicit spark: SparkSession) {

    def replaceNullOnString: DataFrame = {
      val stringCols = df
        .schema
        .filter(_.dataType.typeName == "string")
        .map(_.name)

      stringCols.foldLeft(df) { case (acc, x) =>
        acc.withColumn(x, when(col(x).isNull, lit("NULL")).otherwise(col(x)))
      }
    }

    def bulkColumnTransform(fn: String => Column, cols: Iterable[String] = df.columns): DataFrame = {
      cols.foldLeft(df)((acc, x) => acc.withColumn(x, fn(x)))
    }


    def makeZeroOnDouble(cols: Iterable[String], datatype: DataType = DoubleType): DataFrame = {
      df.bulkColumnTransform(c => coalesce(col(c), lit(0)) cast datatype, cols)
    }

    def makeNullOnString(cols: Iterable[String]): DataFrame = {
      df.bulkColumnTransform(c => coalesce(col(c), lit("NULL")), cols)
    }

    def transformWaveColumn: DataFrame = {
      val schema = df.schema.map(s => (s.name, s.dataType.simpleString))

      val stringCols = schema.filter(_._2 == "string").map(_._1)
      val doubleCols = schema.filter(_._2 == "double").map(_._1)
      df.makeZeroOnDouble(doubleCols)
        .makeNullOnString(stringCols)
    }
    

    def replaceDateForSpark3Support: DataFrame = {
      val dateCols = df
        .schema
        .filter(_.dataType.typeName == "date")
        .map(_.name)
      dateCols.foldLeft(df) { case (acc, x) =>
        acc.withColumn(x, when(col(x) < lit("1900-01-01").cast(DataTypes.DateType), to_date(lit("1900-01-01"), "yyyy-MM-dd")).otherwise(col(x)))
      }
    }

    def replaceTimestampForSpark3Support: DataFrame = {
      val timestampCols = df
        .schema
        .filter(_.dataType.typeName == "timestamp")
        .map(_.name)
      timestampCols.foldLeft(df) { case (acc, x) =>
        acc.withColumn(x, when(col(x) < Date.valueOf("1900-01-01"), to_timestamp(lit(Date.valueOf("1900-01-01")), "yyyy-MM-dd HH:mm:ss.S")).otherwise(col(x)))
      }
    }



  def bulkPrefix(prefix: String, ls: Seq[String] = Nil): DataFrame = {
    df.bulkColumnRenamed(prefix + "_" + _, ls)
  }

  def bulkRipPrefix(prefix: String, ls: Seq[String] = Nil): DataFrame = {
    val p = prefix + "_"
    df.bulkColumnRenamed((s: String) => if (s.startsWith(p)) s.substring(p.length) else s, ls)
  }

  def bulkColumnRenamed(fn: String => String, ls: Seq[String] = Nil): DataFrame = {
    val myLs = if (ls.isEmpty) df.columns.toList else ls
    myLs.foldLeft(df)((acc, c) => acc.withColumnRenamed(c, fn(c)))
  }
  
  def bulkColumnStringToTimeStamp(ls: Seq[String] = Nil): DataFrame = {
    val myLs = if (ls.isEmpty) df.columns.toList else ls
    myLs.foldLeft(df)((acc, c) => acc.withColumn(c, col(c).cast(TimestampType)))
  }

  def getFirstRow(partition: Seq[Column] = Seq(), order: Seq[Column] = Seq()): DataFrame = {
    val wd = Window.partitionBy(partition:_*).orderBy(order:_*)

    df.withColumn("__RN", row_number().over(wd)).filter(col("__RN") === 1).drop("__RN")
  }

    }
}
