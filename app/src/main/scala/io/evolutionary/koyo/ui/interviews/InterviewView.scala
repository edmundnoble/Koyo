package io.evolutionary.koyo.ui.interviews

import java.text.SimpleDateFormat

import android.content.Context
import android.view.View
import android.widget.{LinearLayout, TextView, RelativeLayout}
import io.evolutionary.koyo.R
import io.evolutionary.koyo.parsing.Models
import io.evolutionary.koyo.parsing.Models.Application
import io.evolutionary.koyo.ui.{FindViewForView, ModelView}
import io.evolutionary.koyo.util.DateFormats._

class InterviewView(context: Context) extends LinearLayout(context)
with ModelView[Models.Interview] with FindViewForView {

  private lazy val jobTitleView: TextView = findView(R.id.job_title)
  private lazy val employerView: TextView = findView(R.id.employer)
  private lazy val timeView: TextView = findView(R.id.interview_time)
  private lazy val dateView: TextView = findView(R.id.interview_date)

  View.inflate(context, R.layout.interview_view, this)
  setOrientation(LinearLayout.VERTICAL)

  override def updateModel(m: Models.Interview): Unit = {
    jobTitleView.setText(m.jobTitle)
    employerView.setText(m.employerName)
    val tup = for {
      startTime <- m.startTime
      endTime <- m.endTime
    } yield (SpaceFormat.format(startTime), TimeFormat.format(startTime), TimeFormat.format(endTime))
    tup.fold {
      dateView.setText("")
      timeView.setText("")
    } {
      case (date, startTime, endTime) =>
        dateView.setText(date)
        timeView.setText(s"$startTime - $endTime")
    }
  }

}
