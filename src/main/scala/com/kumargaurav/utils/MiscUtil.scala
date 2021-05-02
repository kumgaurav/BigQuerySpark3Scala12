package com.kumargaurav.utils

import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

/**
 * @author Kumar Gaurav
 * @Date Create May 1, 2021
 */

object MiscUtil {
  type DateFormat = String
  implicit val dateFormat: DateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS"
  def camel2Underscore(text: String): String = text.drop(1).foldLeft(text.headOption.map(_.toLower + "") getOrElse "") {
    case (acc, c) if c.isUpper => acc + "_" + c.toLower
    case (acc, c) => acc + c
  }

  def inform(s: String, withTimeMark: Boolean = true): Unit = {
    val timePart = if (withTimeMark) Calendar.getInstance(TimeZone.getDefault).getTime.toString else ""

    println(s"$timePart $s")
  }

  def convertMilSecondsToHMmSs(milSeconds: Long): String = {
    val seconds: Long = milSeconds / 1000
    val s = seconds % 60
    val m = (seconds / 60) % 60
    val h = (seconds / (60 * 60)) % 24
    f"$h%d:$m%02d:$s%02d"
  }

  def date2string(date: Date)(implicit format: DateFormat) = {
    val f = new SimpleDateFormat(format)
    f.setTimeZone(TimeZone.getDefault)
    f.format(date)
  }

  def string2date(str: String)(implicit format: DateFormat) = {
    val f = new SimpleDateFormat(format)
    f.setTimeZone(TimeZone.getDefault)
    f.parse(str)
  }
}