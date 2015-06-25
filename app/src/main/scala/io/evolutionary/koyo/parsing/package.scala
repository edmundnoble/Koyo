package io.evolutionary.koyo

import java.text.SimpleDateFormat
import java.util.{Calendar, GregorianCalendar, Locale, Date}

import scala.util.Try

package object parsing {
  val SpaceFormat = new SimpleDateFormat("d MMM yyyy", Locale.getDefault)
  val DashFormat = new SimpleDateFormat("d-MMM-yyyy", Locale.getDefault)
  val TimeFormat = new SimpleDateFormat("HH:mm a")

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