package io.evolutionary.koyo.parsing

import java.net.URL

import android.view.View
import io.evolutionary.koyo._
import io.evolutionary.koyo.parsing.Models.JobSearched

object JobSearchPage extends TablePage[Models.JobSearched, Unit] {

  override def tableNames = Map(() -> "UW_CO_JOBRES_VW$scroll$0")

  override def url: URL = Jobmine.Links.JobSearch

  override def tablesToRows(tables: RawTables): Seq[Models.JobSearched] = {
    import TableHeaders._
    val jobs = tables.values.flatMap(_.map {
      row =>
        for {
          jobId <- row.get(JobSearch.JobIdentifier).flatMap(_.parseInt)
          jobTitle <- row.get(Common.JobTitle)
          employer <- row.get(Common.Employer)
          jobStatus <- row.get(Common.JobStatus)
          openings <- row.get(JobSearch.Openings).flatMap(_.parseInt)
          numApps <- row.get(JobSearch.NumApps).flatMap(_.parseInt)
          shortListText <- row.get(JobSearch.ShortList)
          shortListed = shortListText.isEmpty
          location <- row.get(JobSearch.Location)
          unit <- row.get(JobSearch.UnitName)
        } yield JobSearched(jobId, jobTitle, employer, numApps, openings, jobStatus, shortListed, unit)
    })
    jobs.flatten.toSeq
  }
}
