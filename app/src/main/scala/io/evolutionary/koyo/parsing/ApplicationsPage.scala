package io.evolutionary.koyo.parsing

import java.net.URL

import android.view.View
import io.evolutionary.koyo.Jobmine
import io.evolutionary.koyo._

object ApplicationsPage extends TablePage[Models.Application] {
  sealed trait Tables

  case object AllApps extends Tables

  case object ActiveApps extends Tables

  override type TableType = Tables

  override def tableNames: Map[TableType, String] =
    Map(
      AllApps -> "UW_CO_APPS_VW2$scrolli$0",
      ActiveApps -> "UW_CO_SINT_CANC$scroll$0")

  override def url: URL = Jobmine.Links.Applications

  override def tablesToRows(rows: Map[TableType, Seq[Map[String, String]]]): Seq[Models.Application] = {
    import TableHeaders._
    val filteredRows = rows.getOrElse(AllApps, Seq.empty[Map[String, String]])
    val applications = filteredRows map { row =>
      for {
        jobId <- row.get(Common.JobId).flatMap(_.parseInt)
        jobTitle <- row.get(Common.JobTitle)
        employer <- row.get(Common.Employer)
        unit <- row.get(Applications.Unit)
        term <- row.get(Applications.Term)
        jobStatus <- row.get(Common.JobStatus)
        appStatus <- row.get(Applications.AppStatus)
        lastDay <- row.get(Applications.LastDayToApply)
        numApps <- row.get(Applications.NumApps).flatMap(_.parseInt)
      } yield Models.Application(
        jobId, jobTitle, employer, jobStatus,
        unit, term, appStatus, lastDay, numApps)
    }
    applications.flatten
  }
}
