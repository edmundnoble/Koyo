package io.evolutionary.koyo.parsing
import scalaz._
import Scalaz._

object TableHeaders {


  object Common {
    val JobId = "Job ID"
    val JobTitle = "Job Title"
    val Employer = "Employer"
    val JobStatus = "Job Status"
  }

  object Applications {
    val Unit = "Unit"
    val Term = "Term"
    val AppStatus = "App. Status"
    val ViewDetails = "View Details"
    val LastDayToApply = "Last Day to Apply"
    val NumApps = "# Apps"
    val ViewPackage = "View Package"
  }

  object Interviews {
    val EmployerName = "Employer"
    val Date = "Date"
    val InterviewType = "Type"
    val SelectTime = "Interview Select Time"
    val StartTime = "Start Time"
    val EndTime = "Finished Time"
    val Length = "Length"
    val Room = "Room Info"
    val Instructions = "Instructions"
    val Interviewer = "Interviewer"
  }

  object Shortlist {
    val JobIdentifier = "Job ID"
    val UnitName = "Unit"
    val Location = "Job Location"
    val Apply = "Application Status"
    val LastDayToApply = "Last Date to Apply"
  }

  object JobSearch {
    val Openings = "Openings"
    val ShortList = "Short List"
    val UnitName = "Unit"
  }

  object Rankings {
    val EmployerRank = "Rank"
    val StudentRank = "&nbsp"
    val Location = "Work location"
    val WorkTermSupport = "Work Term Supp"
    val OpenDate = "Open Date"
    val OpenTime = "Open Time"
    val CloseDate = "Close Date"
    val CloseTime = "Close Time"
  }

  val Blank = ""
}
