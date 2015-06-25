package io.evolutionary.koyo.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import io.evolutionary.koyo.parsing.{ApplicationsPage, Models}
import io.evolutionary.koyo.parsing.Models.Application

abstract class ApplicationsAdapter(var applications: Vector[Models.Application]) extends RecyclerView.Adapter[ViewHolder] {

  override def getItemCount: Int = applications.length

}
