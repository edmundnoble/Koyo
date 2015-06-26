package io.evolutionary.koyo.ui

import android.content.Context
import android.util.AttributeSet
import android.view.{LayoutInflater, View}
import android.widget
import android.widget.{TextView, RelativeLayout}
import io.evolutionary.koyo.R
import io.evolutionary.koyo.parsing.Models

class ApplicationView(ctx: Context,
                      var _application: Option[Models.Application] = None) extends RelativeLayout(ctx) with GetViewForView {
  private var jobTitleView: Option[TextView] = None
  private var employerView: Option[TextView] = None
  private var appStatusView: Option[TextView] = None
  private var jobStatusView: Option[TextView] = None

  val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE).asInstanceOf[LayoutInflater]
  inflater.inflate(R.layout.application_view, this)

  override def onFinishInflate(): Unit = {
    super.onFinishInflate()
    jobTitleView = Some(getView(R.id.job_title))
    employerView = Some(getView(R.id.employer))
    appStatusView = Some(getView(R.id.app_status))
    jobStatusView = Some(getView(R.id.job_status))
    application.foreach(updateText)
  }

  def updateText(app: Models.Application): Unit = {
    jobTitleView.foreach(_.setText(app.jobTitle))
    employerView.foreach(_.setText(app.employer))
    appStatusView.foreach(_.setText(app.appStatus))
    jobStatusView.foreach(_.setText(app.jobStatus))
  }

  def application = _application

  def application_=(app: Option[Models.Application]) = {
    this._application = app
    app.foreach(updateText)
  }

}