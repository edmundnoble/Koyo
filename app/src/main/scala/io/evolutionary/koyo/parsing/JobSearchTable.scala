package io.evolutionary.koyo.parsing

import java.net.URL

import android.view.View
import io.evolutionary.koyo._

object JobSearchPage extends TablePage {
  sealed trait Tables
  case object JobSearch extends Tables
  override type TableType = Tables

  override def tableNames = Map(JobSearch -> "UW_CO_JOBRES_VW$scroll$0")

  override type ViewElement = View

  override def url: URL = Jobmine.Links.JobSearch

  override def rowToView(tableType: TableType, map: Map[String, String]): Option[ViewElement] = ???
}
