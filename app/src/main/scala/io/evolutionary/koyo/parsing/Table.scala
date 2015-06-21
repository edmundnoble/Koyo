package io.evolutionary.koyo.parsing

import android.view.View

import scalaz._
import Scalaz._

trait Table {
  type ViewElement <: View

  def tableName: String

  def rowToView(map: Map[String, String]): Option[ViewElement]
}

