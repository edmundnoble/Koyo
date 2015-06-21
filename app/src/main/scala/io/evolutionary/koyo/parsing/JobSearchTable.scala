package io.evolutionary.koyo.parsing

import java.net.URL

import android.view.View
import io.evolutionary.koyo._

object JobSearchTable extends Table {
  override def tableName = "UW_CO_JOBRES_VW$scroll$0"

  type ViewElement = View

  override def url: URL = Jobmine.Links.JobSearch

  override def rowToView(map: Map[String, String]): Option[ViewElement] = ???
}
