package io.evolutionary.koyo.parsing

import java.net.URL

import android.view.View
import io.evolutionary.koyo._

object InterviewPage extends TablePage {

  sealed trait Tables

  case object Interview extends Tables

  case object InterviewGroup extends Tables

  case object InterviewSpecial extends Tables

  case object InterviewCancelled extends Tables

  type TableType = Tables

  override def tableNames =
    Map(Interview -> "UW_CO_STUD_INTV$scroll$0",
      InterviewGroup -> "UW_CO_GRP_STU_V$scroll$0",
      InterviewSpecial -> "UW_CO_NSCHD_JOB$scroll$0",
      InterviewCancelled -> "UW_CO_SINT_CANC$scroll$0")


  type ViewElement = View

  override def url: URL = Jobmine.Links.Interviews

  override def rowToView(tableType: TableType, map: Map[String, String]): Option[ViewElement] = ???
}

