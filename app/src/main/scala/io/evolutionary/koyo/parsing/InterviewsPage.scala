package io.evolutionary.koyo.parsing

import java.net.URL
import java.util.Date

import android.view.View
import io.evolutionary.koyo._
import io.evolutionary.koyo.parsing.TableHeaders.Common

sealed trait InterviewTables

case object InterviewNormal extends InterviewTables

case object InterviewGroup extends InterviewTables

case object InterviewSpecial extends InterviewTables

case object InterviewCancelled extends InterviewTables

object InterviewsPage extends TablePage[Models.Interview, InterviewTables] {

  sealed trait Tables

  type TableType = Tables

  override def tableNames =
    Map(InterviewNormal -> "UW_CO_STUD_INTV$scroll$0",
      InterviewGroup -> "UW_CO_GRP_STU_V$scroll$0",
      InterviewSpecial -> "UW_CO_NSCHD_JOB$scroll$0",
      InterviewCancelled -> "UW_CO_SINT_CANC$scroll$0")

  override def url: URL = Jobmine.Links.Interviews

  override def tablesToRows(tables: RawTables): Seq[Models.Interview] = {
    import TableHeaders._
    val rowsWithInterviewStatus = tables.map {
      case (tableType, row) =>
        val interviewType = tableType match {
          case `InterviewNormal` => "Normal"
          case `InterviewGroup` => "Group"
          case `InterviewSpecial` => "Special"
          case `InterviewCancelled` => "Cancelled"
        }
        (interviewType, row)
    }
    val interviews = rowsWithInterviewStatus flatMap {
      case (interviewType, rows) =>
        rows.map { row =>
          for {
            jobId <- row.get(Common.JobId).flatMap(_.parseInt)
            jobTitle <- row.get(Common.JobTitle)
            employer <- row.get(Interviews.EmployerName)
            interviewDate = row.get(Interviews.Date).flatMap(_.parseDate)
            startTime = interviewDate.flatMap(date => row.get(Interviews.StartTime).flatMap(_.parseTime(date)))
            length = row.get(Interviews.Length).flatMap(_.parseInt)
            endTime = for {
              start <- startTime
              len <- length
            } yield new Date(start.getTime + len * 1000 * 60)
            room <- row.get(Interviews.Room)
            instructions <- row.get(Interviews.Instructions)
            jobStatus <- row.get(Common.JobStatus)
            interviewer <- row.get(Interviews.Interviewer)
          } yield Models.Interview(
            jobId, jobTitle, employer, interviewType,
            interviewDate, startTime, endTime, interviewer, room,
            instructions, jobStatus)
        }
    }
    interviews.flatten.toSeq
  }

}

