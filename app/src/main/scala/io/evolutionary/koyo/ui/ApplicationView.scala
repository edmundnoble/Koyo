package io.evolutionary.koyo.ui

import android.content.Context
import android.util.AttributeSet
import android.view.{LayoutInflater, View}
import android.widget
import android.widget.{LinearLayout, TextView, RelativeLayout}
import io.evolutionary.koyo.R
import io.evolutionary.koyo.parsing.Models
import io.evolutionary.koyo.parsing.Models.Application

class ApplicationView(ctx: Context,
                      var application: Models.Application) extends LinearLayout(ctx) with GetViewForView with ModelView[Models.Application] {

  private lazy val jobTitleView: TextView = getView(R.id.job_title)
  private lazy val employerView: TextView = getView(R.id.employer)
  private lazy val appStatusView: TextView = getView(R.id.app_status)
  private lazy val jobStatusView: TextView = getView(R.id.job_status)

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