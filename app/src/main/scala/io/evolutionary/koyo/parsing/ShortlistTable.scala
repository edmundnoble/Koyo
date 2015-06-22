package io.evolutionary.koyo.parsing

import java.net.URL

import android.view.View
import io.evolutionary.koyo._

object ShortlistPage extends TablePage {
  override def tableNames = Map(Shortlist -> "UW_CO_STUJOBLST$scrolli$0")
  sealed trait Tables
  case object Shortlist extends Tables
  type ViewElement = View
  type TableType = Tables

  override def url: URL = Jobmine.Links.Shortlist

  override def rowToView(tableType: TableType, map: Map[String, String]): Option[ViewElement] = ???
}
