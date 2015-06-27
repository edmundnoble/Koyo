package io.evolutionary.koyo.util

import java.text.SimpleDateFormat
import java.util.{TimeZone, Locale}

object DateFormats {
  lazy val SpaceFormat = {
    val format = new SimpleDateFormat("d MMM yyyy", Locale.getDefault)
    format.setTimeZone(EST)
    format
  }
  lazy val DashFormat = {
    val format = new SimpleDateFormat("d-MMM-yyyy", Locale.getDefault)
    format.setTimeZone(EST)
    format
  }
  lazy val TimeFormat = {
    val format = new SimpleDateFormat("hh:mm a")
    format.setTimeZone(EST)
    format
  }
  lazy val EST: TimeZone =
    TimeZone.getTimeZone("GMT")
}