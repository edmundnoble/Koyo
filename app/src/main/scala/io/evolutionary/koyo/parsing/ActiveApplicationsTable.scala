package io.evolutionary.koyo.parsing

import android.view.View

object ActiveApplicationsTable extends Table {
  type ViewElement = View
  override def tableName = "UW_CO_SINT_CANC$scroll$0"
  override def rowToView(row: Map[String, String]) = None




}
