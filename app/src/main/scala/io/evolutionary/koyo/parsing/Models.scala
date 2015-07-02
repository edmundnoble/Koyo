package io.evolutionary.koyo.parsing

import java.util.Date

object Models {

  case class Application(jobId: Int, jobTitle: String, employer: String, jobStatus: String,
                         unit: String, term: String, appStatus: String, lastDayToApply: String,
                         numApps: Int)

  case class JobSearched(jobId: Int, jobTitle: String, employer: String, numApps: Int, openings: Int, jobStatus: String,
                         shortListed: Boolean, unit: String)

  case class Interview(jobId: Int, jobTitle: String, employerName: String, interviewType: String,
                       interviewDate: Option[Date], startTime: Option[Date], endTime: Option[Date],
                       interviewer: String, room: String, instructions: String, jobStatus: String)

  case class ShortlistedJob(jobId: Int, unit: String, location: String, applied: Boolean,
                            lastDayToApply: Date)

  case class Ranking(jobId: Int, jobTitle: String, employer: String, location: String,
                     studentRanking: Option[String], employerRanking: String, workTermSupport: String,
                     openDate: Option[Date], openTime: Option[Date], closeDate: Option[Date], closeTime: Option[Date])

}