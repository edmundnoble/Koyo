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

class ModelAdapter[Model: Manifest, V <: View with ModelView[Model]](context: Context, makeView: (Context, Model) => V)
  extends ArrayAdapter[Model](context, 0, new util.ArrayList[Model]()) {
  def models = (0 until getCount) map getItem

  def models_=(models: Seq[Model]): Unit = {
    val newModelSet = models.toSet
    val oldModelSet = this.models.toSet
    val toRemove = oldModelSet.diff(newModelSet)
    val toAdd = newModelSet.diff(oldModelSet)
    toRemove.foreach(remove)
    toAdd.foreach(add)
    notifyDataSetChanged()
  }

  override def getView(pos: Int, convertView: View, parent: ViewGroup): View = {
    val model = getItem(pos)
    Option(convertView).fold(makeView(parent.getContext, model)) { cview =>
      val asAppView = cview.asInstanceOf[V]
      asAppView.updateModel(model)
      asAppView
    }
  }
}
