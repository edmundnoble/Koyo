package io.evolutionary.koyo.parsing

import java.net.URL

import android.view.View
import io.evolutionary.koyo._

object InterviewTable extends Table {
  override def tableName = "UW_CO_STUD_INTV$scroll$0"

  type ViewElement = View

  override def url: URL = Jobmine.Links.Interviews

  override def rowToView(map: Map[String, String]): Option[ViewElement] = ???
}

object InterviewGroupTable extends Table {

  override def tableName = "UW_CO_GRP_STU_V$scroll$0"

  type ViewElement = View

  override def url: URL = Jobmine.Links.Interviews

  override def rowToView(map: Map[String, String]): Option[ViewElement] = ???
}

object InterviewSpecialTable extends Table {
  override def tableName = "UW_CO_NSCHD_JOB$scroll$0"

  type ViewElement = View

  override def url: URL = Jobmine.Links.Interviews

  override def rowToView(map: Map[String, String]): Option[ViewElement] = ???
}

object InterviewCancelledTable extends Table {
  override def tableName = "UW_CO_SINT_CANC$scroll$0"

  type ViewElement = View

  override def url: URL = Jobmine.Links.Interviews

  override def rowToView(map: Map[String, String]): Option[ViewElement] = ???
}
