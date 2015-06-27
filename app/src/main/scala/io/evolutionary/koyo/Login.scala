package io.evolutionary.koyo

import java.net.URL
import java.security.GeneralSecurityException
import javax.net.ssl.SSLContext

import android.util.Log
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

  def login(username: String, password: String, taskNum: Int)(implicit client: OkHttpClient): Task[Writer[Int, LoginStatus]] = {
    Log.d("Login", "Logging in...")
    val encoding = new FormEncodingBuilder()
      .add("submit", "Submit")
      .add("timezoneOffset", "480")
      .add("httpPort", "")
      .add("userid", username)
      .add("pwd", password)
      .build()
    val request = new Request.Builder()
      .url(Jobmine.Links.Login)
      .post(encoding)
      .build()
    if (username == "test") Task.delay(LoggedIn) // DONOTSHIP
    else Jobmine.asyncRequest(request) map { loginResponse =>
      Log.d("Login", "Login complete!")
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
