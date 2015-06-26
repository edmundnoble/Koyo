package io.evolutionary.koyo.parsing

import java.net.URL

import android.view.View
import io.evolutionary.koyo.Jobmine
import io.evolutionary.koyo._

sealed trait ApplicationsTables

case object AllApplicationsTable extends ApplicationsTables

case object ActiveApplicationsTable extends ApplicationsTables

object ApplicationsPage extends TablePage[Models.Application, ApplicationsTables] {

  override def tableNames: Map[ApplicationsTables, String] =
    Map(
      AllApplicationsTable -> "UW_CO_APPS_VW2$scrolli$0",
      ActiveApplicationsTable -> "UW_CO_STU_APPSV$scroll$0"
    )

  override def url: URL = Jobmine.Links.Applications

  override def tablesToRows(tables: RawTables): Seq[Models.Application] = {
    import TableHeaders._
    val applications = tables.values.flatMap(rows => rows.map { row =>
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
    })
    applications.flatten.toSeq
  }
}
