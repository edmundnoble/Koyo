package io.evolutionary.koyo.ui.interviews

import android.content.Context
import android.view.View
import android.widget.{LinearLayout, TextView, RelativeLayout}
import io.evolutionary.koyo.R
import io.evolutionary.koyo.parsing.Models
import io.evolutionary.koyo.parsing.Models.Application
import io.evolutionary.koyo.ui.{FindViewForView, ModelView}

class InterviewView(context: Context) extends LinearLayout(context)
with ModelView[Models.Interview] with FindViewForView {

  private lazy val jobTitleView: TextView = findView(R.id.job_title)
  private lazy val employerView: TextView = findView(R.id.employer)
  private lazy val startTimeView: TextView = findView(R.id.start_time)
  private lazy val interviewDateView: TextView = findView(R.id.interview_date)

  View.inflate(context, R.layout.interview_view, this)
  setOrientation(LinearLayout.VERTICAL)

  override def updateModel(m: Models.Interview): Unit = {
    jobTitleView.setText(m.jobTitle)
    employerView.setText(m.employerName)
    startTimeView.setText(m.startTime.toString)
    interviewDateView.setText(m.interviewDate.toString)
  }

}
