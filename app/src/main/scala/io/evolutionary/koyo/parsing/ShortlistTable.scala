package io.evolutionary.koyo.parsing

import java.net.URL

import android.view.View
import io.evolutionary.koyo._

object ShortlistTable extends Table {
  override def tableName = "UW_CO_STUJOBLST$scrolli$0"

  type ViewElement = View

  override def url: URL = Jobmine.Links.Shortlist

  override def rowToView(map: Map[String, String]): Option[ViewElement] = ???
}
