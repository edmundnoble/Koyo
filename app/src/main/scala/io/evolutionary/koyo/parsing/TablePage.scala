package io.evolutionary.koyo.parsing

import java.net.URL

import android.view.View

import scalaz._
import Scalaz._

trait TablePage[RowModel, TableType] { self =>

  type RawRow = Map[String, String]
  type RawTables = Map[TableType, Seq[RawRow]]

  def tableNames: Map[TableType, String]

  def url: URL

  def tablesToRows(tables: RawTables): Seq[RowModel]

  def <<<(fun: (RawTables) => RawTables): TablePage[RowModel, TableType] = new TablePage[RowModel, TableType] {
    def tableNames = self.tableNames
    def url = self.url
    def tablesToRows(tables: RawTables) = self.tablesToRows(fun(tables))
  }

  def >>>[T](fun: (RowModel => T)) = new TablePage[T, TableType] {
    def tableNames = self.tableNames
    def url = self.url
    def tablesToRows(tables: RawTables) = self.tablesToRows(tables).map(fun)
  }


}
