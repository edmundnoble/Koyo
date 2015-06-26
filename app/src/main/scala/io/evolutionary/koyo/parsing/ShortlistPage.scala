package io.evolutionary.koyo.parsing

import java.net.URL

import android.view.View
import io.evolutionary.koyo._
import io.evolutionary.koyo.parsing.Models.ShortlistedJob

object ShortlistPage extends TablePage[Models.ShortlistedJob, Unit] {
  override def tableNames = Map(() -> "UW_CO_STUJOBLST$scrolli$0")

  override def url: URL = Jobmine.Links.Shortlist

  override def tablesToRows(tables: RawTables): Seq[Models.ShortlistedJob] = {
    import TableHeaders._
    val shortlisted = tables.values.head.flatMap(row =>
      for {
        jobId <- row.get(Shortlist.JobIdentifier).flatMap(_.parseInt)
        unit <- row.get(Shortlist.UnitName)
        location <- row.get(Shortlist.Location)
        applyString <- row.get(Shortlist.Apply)
        applied = applyString == "Already Applied"
        lastDayToApply <- row.get(Shortlist.LastDayToApply).flatMap(_.parseDate)
      } yield ShortlistedJob(jobId, unit, location, applied, lastDayToApply)
    )
    shortlisted.toSeq
  }
}
