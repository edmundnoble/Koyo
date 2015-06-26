package io.evolutionary.koyo.ui

import android.view.View

trait ModelView[Model] { self: View =>
  def updateModel(m: Model): Unit
}
