package io.evolutionary.koyo.ui

import java.util

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.{View, ViewGroup}
import android.widget.{ArrayAdapter, ListAdapter, ListView}
import io.evolutionary.koyo.parsing.Models
import scala.collection.JavaConverters._
import scala.util.Try

class ModelAdapter[Model, V <: View with ModelView[Model]](context: Context, makeView: (Context) => V)
  extends ArrayAdapter[Model](context, 0, new util.ArrayList[Model]()) {
  def models = (0 until getCount) map getItem

  def models_=(models: Seq[Model]): Unit = {
    this.models.foreach(remove)
    models.foreach(add)
    notifyDataSetChanged()
  }

  override def getView(pos: Int, convertView: View, parent: ViewGroup): View = {
    val model = getItem(pos)
    val view = Option(convertView).fold {
      makeView(context)
    } { cview =>
      cview.asInstanceOf[V]
    }
    view.updateModel(model)
    view
  }
}
