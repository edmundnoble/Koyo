package io.evolutionary.koyo.parsing

import java.net.URL

import android.view.View

import scalaz._
import Scalaz._

trait TablePage {
  type RowModel
  type TableType

  def tableNames: Map[TableType, String]
  def url: URL

  def tableToViews(rows: Seq[(TableType, Map[String, String])]): Seq[RowModel]
}
