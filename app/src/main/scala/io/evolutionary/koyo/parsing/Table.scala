package io.evolutionary.koyo.parsing

import java.net.URL

import android.view.View

import scalaz._
import Scalaz._

trait Table {
  type ViewElement <: View

  def tableName: String
  def url: URL

  def rowToView(map: Map[String, String]): Option[ViewElement]
}

