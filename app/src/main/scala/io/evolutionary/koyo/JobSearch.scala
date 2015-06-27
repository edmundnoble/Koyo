package io.evolutionary.koyo

import com.squareup.okhttp.{FormEncodingBuilder, Request}
import io.evolutionary.koyo.parsing.{JobSearchPage, Models}
import org.jsoup.nodes.Document

import scalaz.Writer
import scalaz.concurrent.Task

object JobSearch {

  val LocationProperty = "UW_CO_JOBSRCH_UW_CO_LOCATION"
  val JobTypeProperty = "UW_CO_JOBSRCH_UW_CO_LOCATION"
  val DisciplinesProperties = Seq("UW_CO_JOBSRCH_UW_CO_ADV_DISCP1",
    "UW_CO_JOBSRCH_UW_CO_ADV_DISCP2",
    "UW_CO_JOBSRCH_UW_CO_ADV_DISCP3")
  val FilterProperty = "UW_CO_JOBSRCH_UW_CO_JS_JOBSTATUS"
  val TermProperty = "UW_CO_JOBSRCH_UW_CO_WT_SESSION"
  val EmployerProperty = "UW_CO_JOBSRCH_UW_CO_EMPLYR_NAME"
  val TitleProperty = "UW_CO_JOBSRCH_UW_CO_JOB_TITLE"

  case class Filter(pretty: String, jobmineValue: String)

  val Approved = Filter("Approved", "APPR")
  val AppsAvail = Filter("Apps Avail", "APPA")
  val Cancelled = Filter("Cancelled", "CANC")
  val Posted = Filter("Posted", "POST")

  case class JobType(asString: String, asInt: Int)

  val Coop = JobType("Co-op", 1)
  val CoopArch = JobType("Co-op ARCH", 2)
  val CoopCa = JobType("Co-op CA", 2)
  val CoopTeach = JobType("Co-op TEACH", 2)
  val CoopPharm = JobType("Co-op PHARM", 9)
  val CoopUae = JobType("Co-op UAE", 10)
  val Alumni = JobType("Alumni", 5)
  val Graduating = JobType("Graduating", 6)
  val Other = JobType("Other", 7)
  val Summer = JobType("Summer", 8)

  case class JobLevel private[JobSearch](friendly: String, jobmineProperty: String)

  val JuniorLevel = JobLevel("Junior", "UW_CO_JOBSRCH_UW_CO_COOP_JR$chk")
  val IntermediateLevel = JobLevel("Intermediate", "UW_CO_JOBSRCH_UW_CO_COOP_INT$chk")
  val SeniorLevel = JobLevel("Senior", "UW_CO_JOBSRCH_UW_CO_COOP_SR$chk")
  val BachelorsLevel = JobLevel("Bachelors", "UW_CO_JOBSRCH_UW_CO_BACHELOR$chk")
  val MastersLevel = JobLevel("Masters", "UW_CO_JOBSRCH_UW_CO_MASTERS$chk")
  val PhDLevel = JobLevel("PhD", "UW_CO_JOBSRCH_UW_CO_PHD$chk")

  val AllLevels = Set(JuniorLevel, IntermediateLevel, SeniorLevel, BachelorsLevel, MastersLevel, PhDLevel)

  case class SearchParams private[JobSearch](disciplines: Seq[String], term: String,
                                              location: String, filter: Filter, jobType: String,
                                              employer: String, title: String, levels: Set[JobLevel])

  def getStateNum(html: Document): Int = ???

  def searchForJobs(params: SearchParams, stateNum: Int, icsid: Int): Task[(Seq[Models.JobSearched], Int)] = {
    val requestBodyBuilder = new FormEncodingBuilder()
    val nonRequestedLevels = AllLevels diff params.levels
    nonRequestedLevels.foreach(level => requestBodyBuilder.add(level.jobmineProperty, "N"))
    params.levels.foreach(level => requestBodyBuilder.add(level.jobmineProperty, "Y"))
    val requestBody = requestBodyBuilder.add("ICAction", "UW_CO_JOBSRCHDW_UW_CO_DW_SRCHBTN")
      .add("ICElementNum", "0")
      .add("ICAJAX", "1")
      .add("ICStateNum", stateNum)
      .add("ICSID", icsid)
      .add(LocationProperty, params.location)
      .add(JobTypeProperty, params.jobType)
      .add(FilterProperty, params.filter.jobmineValue)
      .add(TermProperty, params.term)
      .add(TitleProperty, params.title)
      .build()

    val request = new Request.Builder()
      .url(Jobmine.Links.JobSearch)
      .post(requestBody)
      .build()

    Jobmine.asyncRequest(request) map { response =>
      val html = response.body().html
      val rows = Jobmine.parseTablePageRows(JobSearchPage, html)
      println(response.body().string())
      (rows, getStateNum(response.body()))
    }
  }
}