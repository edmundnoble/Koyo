package io.evolutionary.koyo.parsing

import java.net.URL

import android.view.View
import io.evolutionary.koyo._
import io.evolutionary.koyo.parsing.Models.JobSearched

object JobSearchPage extends TablePage {

  sealed trait Tables

  case object JobSearchTable extends Tables

  override type TableType = Tables

  override def tableNames = Map(JobSearchTable -> "UW_CO_JOBRES_VW$scroll$0")

  override type RowModel = Models.JobSearched

  override def url: URL = Jobmine.Links.JobSearch

  override def tablesToRows(tables: Map[TableType, Seq[Map[String, String]]]): Seq[JobSearched] = {
    import TableHeaders._
    val jobs = tables.values.flatMap(_.map {
      row =>
        for {
          jobId <- row.get(Common.JobId).flatMap(_.parseInt)
          jobTitle <- row.get(Common.JobTitle)
          employer <- row.get(Common.Employer)
          jobStatus <- row.get(Common.JobStatus)
          openings <- row.get(JobSearch.Openings).flatMap(_.parseInt)
          shortListed = ???
          unit <- row.get(JobSearch.UnitName)
        } yield JobSearched(jobId, jobTitle, employer, jobStatus, openings, shortListed, unit)
    })
    jobs.flatten.toSeq
  }
}
