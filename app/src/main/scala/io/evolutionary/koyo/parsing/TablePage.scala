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
    override def tableNames = self.tableNames
    override def url = self.url
    override def tablesToRows(tables: RawTables) = self.tablesToRows(fun(tables))
  }

  def >>>[T](fun: (Seq[RowModel] => Seq[T])) = new TablePage[T, TableType] {
    override def tableNames = self.tableNames
    override def url = self.url
    override def tablesToRows(tables: RawTables) = fun(self.tablesToRows(tables))
  }

  def before(fun: (RawTables) => RawTables): TablePage[RowModel, TableType] = <<<(fun)

  def after[T](fun: (Seq[RowModel] => Seq[T])) = >>>(fun)


}
