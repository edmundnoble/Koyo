package io.evolutionary.koyo.parsing

import java.net.URL

import android.view.View

import scalaz._
import Scalaz._

trait TablePage[RowModel] {
  type TableType

  def tableNames: Map[TableType, String]
  def url: URL

  def tablesToRows(tables: Map[TableType, Seq[Map[String, String]]]): Seq[RowModel]
}
