package io.evolutionary.koyo.ui

import android.view.View

trait FindViewForView { self: View =>
  protected def findView[T <: View](id: Int): T = findViewById(id).asInstanceOf[T]
}
