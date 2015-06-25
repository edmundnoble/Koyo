package io.evolutionary.koyo.parsing

import java.net.URL

import android.view.View
import io.evolutionary.koyo._
import io.evolutionary.koyo.parsing.Models.ShortlistedJob

object ShortlistPage extends TablePage {
  override def tableNames = Map(ShortlistTable -> "UW_CO_STUJOBLST$scrolli$0")
  sealed trait Tables
  case object ShortlistTable extends Tables
  type RowModel = Models.ShortlistedJob
  type TableType = Tables

  override def url: URL = Jobmine.Links.Shortlist

  override def tableToViews(rows: Seq[(Tables, Map[String, String])]): Seq[ShortlistedJob] = {
    import TableHeaders._
    val shortlisted = rows.map {
      case (_, row) =>
        for {
          jobId <- row.get(Shortlist.JobIdentifier).flatMap(_.parseInt)
          unit <- row.get(Shortlist.UnitName)
          location <- row.get(Shortlist.Location)
          applyString <- row.get(Shortlist.Apply)
          applied = applyString == "Already Applied"
          lastDayToApply <- row.get(Shortlist.LastDayToApply).flatMap(_.parseDate)
        } yield ShortlistedJob(jobId, unit, location, applied, lastDayToApply)
    }
    shortlisted.flatten
  }
}
