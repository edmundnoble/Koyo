package io.evolutionary.koyo

import java.text.SimpleDateFormat
import io.evolutionary.koyo.util.DateFormats._
import java.util.{Calendar, GregorianCalendar, Locale, Date}

import scala.util.Try

package object parsing {

  implicit class StringParseDate(val str: String) extends AnyVal {
    def parseDate: Option[Date] = {
      Try(SpaceFormat.parse(str)).orElse(Try(DashFormat.parse(str))).toOption
    }

    def parseTime(day: Date): Option[Date] = {
      Try(TimeFormat.parse(str)).map { time: Date =>
        new Date(day.getTime + time.getTime)
      }.toOption
    }
  }

}