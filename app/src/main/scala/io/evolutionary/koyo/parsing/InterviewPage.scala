package io.evolutionary.koyo.parsing

import java.net.URL

import android.view.View
import io.evolutionary.koyo._
import io.evolutionary.koyo.parsing.Models.Interview
import io.evolutionary.koyo.parsing.TableHeaders.Common

object InterviewPage extends TablePage {

  sealed trait Tables

  case object Interview extends Tables

  case object InterviewGroup extends Tables

  case object InterviewSpecial extends Tables

  case object InterviewCancelled extends Tables

  type TableType = Tables

  override def tableNames =
    Map(Interview -> "UW_CO_STUD_INTV$scroll$0",
      InterviewGroup -> "UW_CO_GRP_STU_V$scroll$0",
      InterviewSpecial -> "UW_CO_NSCHD_JOB$scroll$0",
      InterviewCancelled -> "UW_CO_SINT_CANC$scroll$0")


  type RowModel = Models.Interview

  override def url: URL = Jobmine.Links.Interviews

  override def tableToViews(rows: Seq[(Tables, Map[String, String])]): Seq[Interview] = {
    import TableHeaders._
    val rowsWithInterviewStatus = rows.map {
      case (tableType, row) =>
        val interviewType = tableType match {
          case `Interview` => "Normal"
          case `InterviewGroup` => "Group"
          case `InterviewSpecial` => "Special"
          case `InterviewCancelled` => "Cancelled"
        }
        (interviewType, row)
    }
    val interviews = rowsWithInterviewStatus map {
      case (interviewType, row) =>
        for {
          jobId <- row.get(Common.JobId).flatMap(_.parseInt)
          jobTitle <- row.get(Common.JobTitle)
          employer <- row.get(Common.Employer)
          interviewDate = row.get(Interviews.Date).flatMap(_.parseDate)
          startTime = interviewDate.flatMap(date => row.get(Interviews.StartTime).flatMap(_.parseTime(date)))
          endTime = interviewDate.flatMap(date => row.get(Interviews.EndTime).flatMap(_.parseTime(date)))
          room <- row.get(Interviews.Room)
          instructions <- row.get(Interviews.Instructions)
          jobStatus <- row.get(Common.JobStatus)
          interviewer <- row.get(Interviews.Interviewer)
        } yield Models.Interview(
          jobId, jobTitle, employer, interviewType,
          interviewDate, startTime, endTime, interviewer, room,
          instructions, jobStatus)
    }
    interviews.flatten
  }

}

