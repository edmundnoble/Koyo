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

class ApplicationsAdapter(context: Context) extends ArrayAdapter[Models.Application](context, 0, new util.ArrayList[Models.Application]()) {

  def applications = (0 until getCount) map getItem

  def applications_=(apps: Seq[Models.Application]): Unit = {
    val newAppSet = apps.toSet
    val oldAppSet = applications.toSet
    val toRemove = oldAppSet.diff(newAppSet)
    val toAdd = newAppSet.diff(oldAppSet)
    toRemove.foreach(remove)
    toAdd.foreach(add)
    println(s"Added $toAdd")
    println(s"Removed $toRemove")

    notifyDataSetChanged()
  }

  override def getView(pos: Int, convertView: View, parent: ViewGroup): View = {
    val realView = Try(convertView.asInstanceOf[ApplicationView]) getOrElse new ApplicationView(parent.getContext)
    realView.application = Some(applications(pos))
    realView
  }

}
