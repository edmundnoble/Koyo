package io.evolutionary.koyo.ui

import android.content.Context
import android.util.AttributeSet
import android.view.{LayoutInflater, View}
import android.widget
import android.widget.{LinearLayout, TextView, RelativeLayout}
import io.evolutionary.koyo.R
import io.evolutionary.koyo.parsing.Models

class ApplicationView(ctx: Context,
                      var application: Models.Application) extends LinearLayout(ctx) with GetViewForView {

  private lazy val jobTitleView: TextView = getView(R.id.job_title)
  private lazy val employerView: TextView = getView(R.id.employer)
  private lazy val appStatusView: TextView = getView(R.id.app_status)
  private lazy val jobStatusView: TextView = getView(R.id.job_status)

  View.inflate(ctx, R.layout.application_view, this)
  setOrientation(LinearLayout.VERTICAL)
  updateText(application)

  def updateText(app: Models.Application): Unit = {
    jobTitleView.setText(app.jobTitle)
    employerView.setText(app.employer)
    appStatusView.setText(app.appStatus)
    jobStatusView.setText(app.jobStatus)
  }

}