package io.evolutionary.koyo.ui

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import io.evolutionary.koyo.parsing.Models

object ApplicationsAdapter {

  class MyHolder(val view: ApplicationView) extends ViewHolder(view)

}

class ApplicationsAdapter(private var _applications: Seq[Models.Application]) extends RecyclerView.Adapter[ApplicationsAdapter.MyHolder] {

  import ApplicationsAdapter._

  def applications = _applications
  def applications_=(apps: Seq[Models.Application]): Unit = {
    _applications = apps
    notifyDataSetChanged()
  }

  @Override
  def onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder = {
    val v = new ApplicationView(parent.getContext)
    new MyHolder(v)
  }

  @Override
  def onBindViewHolder(holder: MyHolder, position: Int) = {
    holder.view.application = Some(applications(position))
  }

  @Override
  def getItemCount: Int = applications.length

}
