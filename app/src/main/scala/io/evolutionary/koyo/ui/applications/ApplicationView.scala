package io.evolutionary.koyo.ui.applications

import android.content.Context
import android.view.View
import android.widget.{LinearLayout, TextView}
import io.evolutionary.koyo.R
import io.evolutionary.koyo.parsing.Models
import io.evolutionary.koyo.parsing.Models.Application
import io.evolutionary.koyo.ui.{FindViewForView, ModelView}

class ApplicationView(ctx: Context, var application: Models.Application)
  extends LinearLayout(ctx) with FindViewForView with ModelView[Models.Application] {

  private lazy val jobTitleView: TextView = findView(R.id.job_title)
  private lazy val employerView: TextView = findView(R.id.employer)
  private lazy val appStatusView: TextView = findView(R.id.app_status)
  private lazy val jobStatusView: TextView = findView(R.id.job_status)

  View.inflate(ctx, R.layout.application_view, this)
  setOrientation(LinearLayout.VERTICAL)
  updateModel(application)

  override def updateModel(m: Application): Unit = {
    jobTitleView.setText(m.jobTitle)
    employerView.setText(m.employer)
    appStatusView.setText(m.appStatus)
    jobStatusView.setText(m.jobStatus)
  }

}