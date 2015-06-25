package io.evolutionary.koyo
package ui

import android.view.View

trait GetViewForView {
  self: View =>
  def getView[T <: View](id: Int) = findViewById(id).asInstanceOf[T]
}