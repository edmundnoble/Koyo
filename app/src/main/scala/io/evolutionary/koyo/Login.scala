package io.evolutionary.koyo

import java.net.URL

import com.squareup.mimecraft.FormEncoding
import com.squareup.okhttp._

import scala.concurrent.Future
import scalaz.concurrent.{Strategy, Task}
import java.io.IOException
import scalaz._
import Scalaz._

object Login {

  sealed trait LoginStatus

  case object LoggedIn extends LoginStatus

  case object Offline extends LoginStatus

  case object LoggedOut extends LoginStatus

  // Login constants
  val FAILED_LOGIN_STRING = "Signin HTML for JobMine."

  val LOGIN_OFFLINE_MESSAGE = "Invalid signon time for user"

  val DEFAULT_HTML_ENCODER = "UTF-8"

  val FAILED_URL = "Invalid URL - no Node found in"

  val LOGIN_READ_LENGTH = 400

  val LOGIN_ERROR_MSG_SKIP = 3200

  val MAX_LOGIN_ATTEMPTS = 3

  val client = new OkHttpClient()
  type OkCallback = (Throwable \/ Response) => Unit

  object Links {
    val Documents = new URL("https://jobmine.ccol.uwaterloo.ca/psc/SS/EMPLOYEE/WORK/c/UW_CO_STUDENTS.UW_CO_STUDDOCS")
    val Profile = new URL("https://jobmine.ccol.uwaterloo.ca/psc/SS/EMPLOYEE/WORK/c/UW_CO_STUDENTS.UW_CO_STUDENT")
    val Skills = new URL("https://jobmine.ccol.uwaterloo.ca/psc/SS/EMPLOYEE/WORK/c/UW_CO_STUDENTS.UW_CO_STUDENT?PAGE=UW_CO_STU_SKL_MTN")
    val Search = new URL("https://jobmine.ccol.uwaterloo.ca/psc/SS/EMPLOYEE/WORK/c/UW_CO_STUDENTS.UW_CO_JOBSRCH")
    val Shortlist = new URL("https://jobmine.ccol.uwaterloo.ca/psc/SS/EMPLOYEE/WORK/c/UW_CO_STUDENTS.UW_CO_JOB_SLIST")
    val Applications = new URL("https://jobmine.ccol.uwaterloo.ca/psc/SS/EMPLOYEE/WORK/c/UW_CO_STUDENTS.UW_CO_APP_SUMMARY")
    val Interviews = new URL("https://jobmine.ccol.uwaterloo.ca/psc/SS/EMPLOYEE/WORK/c/UW_CO_STUDENTS.UW_CO_STU_INTVS")
    val Rankings = new URL("https://jobmine.ccol.uwaterloo.ca/psc/SS/EMPLOYEE/WORK/c/UW_CO_STUDENTS.UW_CO_RNK2")
    val Descriptions = new URL("https://jobmine.ccol.uwaterloo.ca/psc/SS/EMPLOYEE/WORK/c/UW_CO_STUDENTS.UW_CO_JOBDTLS?UW_CO_JOB_ID=")
    val Login = new URL("https://jobmine.ccol.uwaterloo.ca/psp/SS/?cmd=login&languageCd=ENG&sessionId=")
    val Logout = new URL("https://jobmine.ccol.uwaterloo.ca/psp/SS/?cmd=login&languageCd=ENG&")
  }

  def asyncRequest(request: Request): Task[Response] = {
    Task.async { cb: OkCallback =>
      val call = client.newCall(request)
      call.enqueue(new Callback() {
        override def onFailure(request: Request, e: IOException): Unit = cb(e.left[Response])

        override def onResponse(response: Response): Unit = cb(response.right[Throwable])
      })
    }
  }

  def login(username: String, password: String): Task[LoginStatus] = {
    val encoding = new FormEncodingBuilder()
      .add("submit", "Submit")
      .add("timezoneOffset", "480")
      .add("httpPort", "")
      .add("userid", username)
      .add("pwd", password)
      .build()
    val request = new Request.Builder()
      .url(Links.Login)
      .post(encoding)
      .build()
    asyncRequest(request) map { loginResponse =>
      val respString = loginResponse.body().string()
      if (respString contains FAILED_LOGIN_STRING) {
        if (respString contains LOGIN_OFFLINE_MESSAGE) {
          Offline
        } else {
          LoggedOut
        }
      } else {
        LoggedIn
      }
    }

  }

}
