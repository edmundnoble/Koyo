package io.evolutionary.koyo.parsing

import java.net.URL

import android.view.View
import io.evolutionary.koyo.Jobmine
import io.evolutionary.koyo.parsing.Table

object AllApplicationsTable extends Table {
  override type ViewElement = View

  override def tableName: String = "UW_CO_APPS_VW2$scrolli$0"

  override def url: URL = Jobmine.Links.Applications

  override def rowToView(map: Map[String, String]): Option[ViewElement] = ???
}

object ActiveApplicationsTable extends Table {
  type ViewElement = View

  override def tableName = "UW_CO_SINT_CANC$scroll$0"

  override def url = Jobmine.Links.Applications

  override def rowToView(row: Map[String, String]): Option[ViewElement] = ???
}
