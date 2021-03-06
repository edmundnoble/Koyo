package io.evolutionary.koyo.parsing

import java.net.URL

import io.evolutionary.koyo._

object RankingsPage extends TablePage[Models.Ranking, Unit] {

  override def tableNames: Map[Unit, String] = Map(() -> "UW_CO_STU_RNKV2$scroll$0")

  override def url: URL = Jobmine.Links.Rankings

  override def tablesToRows(tables: RawTables): Seq[Models.Ranking] = {
    import TableHeaders._
    val rankings = tables.values.flatMap(_.map { row =>
        for {
          jobId <- row.get(Common.JobId).flatMap(_.parseInt)
          jobTitle <- row.get(Common.JobTitle)
          employer <- row.get(Common.Employer)
          workLocation <- row.get(Rankings.Location)
          workTermSupport <- row.get(Rankings.WorkTermSupport)
          openDate = row.get(Rankings.OpenDate).flatMap(_.parseDate)
          openTime = openDate.flatMap (date => row.get(Rankings.OpenTime).flatMap(_.parseTime(date)))
          closeDate = row.get(Rankings.CloseDate).flatMap(_.parseDate)
          closeTime = closeDate.flatMap (date => row.get(Rankings.CloseTime).flatMap(_.parseTime(date)))
          employerRank <- row.get(Rankings.EmployerRank)
          studentRank = row.get(Rankings.StudentRank)
        } yield Models.Ranking(jobId, jobTitle, employer, workLocation,
          studentRank, employerRank, workTermSupport, openDate, openTime, closeDate, closeTime)
    })
    rankings.flatten.toSeq
  }

}