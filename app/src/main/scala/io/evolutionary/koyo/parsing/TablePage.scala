package io.evolutionary.koyo.parsing

import java.net.URL

import android.view.View

import scalaz._
import Scalaz._

trait TablePage {
  type ViewElement <: View
  type TableType

  def tableNames: Map[TableType, String]
  def url: URL

  def rowToView(tableType: TableType, map: Map[String, String]): Option[ViewElement]
}

