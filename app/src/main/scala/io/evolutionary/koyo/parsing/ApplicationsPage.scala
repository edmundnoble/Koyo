package io.evolutionary.koyo.parsing

import java.net.URL

import android.view.View
import io.evolutionary.koyo.Jobmine

object ApplicationsPage extends TablePage {
  override type ViewElement = View

  sealed trait Tables

  case object AllApps extends Tables

  case object ActiveApps extends Tables

  override type TableType = Tables

  override def tableNames: Map[TableType, String] =
    Map(
      AllApps -> "UW_CO_APPS_VW2$scrolli$0",
      ActiveApps -> "UW_CO_SINT_CANC$scroll$0")

  override def url: URL = Jobmine.Links.Applications

  override def rowToView(tableType: TableType, map: Map[String, String]): Option[ViewElement] = ???
}
