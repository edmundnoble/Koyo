package io.evolutionary.koyo.parsing

object TableHeaders {

  object Common {
    val JobId = ("job id", "Job ID")
    val JobTitle = ("job title", "Job Title")
    val Employer = ("employer", "Employer")
    val JobStatus = ("job status", "Job Status")
  }

  object Applications {
    val Unit = ("unit", "Unit")
    val Term = ("term", "Term")
    val AppStatus = ("app. status", "Application Status")
    val ViewDetails = ("view details", "View Details")
    val LastDayToApply = ("last day to apply", "Last Day to Apply")
    val NumApps = ("# apps", "Number of Apps")
    val ViewPackage = ("view package", "View Package")
  }

  object Interviews {
    val EmployerName = ("employer name", "Employer")
    val Date = ("date", "Date")
    val InterviewType = ("type", "Type")
    val SelectTime = ("select/view time", "Interview Select Time")
    val StartTime = ("start time", "Start Time")
    val EndTime = ("end time", "Finished Time")
    val Length = ("length", "Length")
    val Room = ("room", "Room Info")
    val Instructions = ("instructions", "Instructions")
    val Interviewer = ("interviewer", "Interviewer")
  }

  object Shortlist {
    val JobIdentifier = ("job identifier", "Job ID")
    val UnitName = ("unit name 1", "Unit")
    val Location = ("location", "Job Location")
    val Apply = ("apply", "Application Status")
    val LastDayToApply = ("last date to apply", "Last Date to Apply")
  }

  object JobSearch {
    val Openings = ("openings", "Openings")
    val ShortList = ("short list", "Short List")
    val UnitName = ("unit name", "Unit")
  }

  val Blank = ("", "")
}
