package io.evolutionary.koyo.ui

import java.net.{CookieHandler, CookiePolicy, CookieManager}

import android.app.Activity
import android.os.{Bundle, Handler}
import com.squareup.okhttp.OkHttpClient
import io.evolutionary.koyo.JobSearch.SearchParams
import io.evolutionary.koyo._
import io.evolutionary.koyo.ui.common.BaseActivity
import io.evolutionary.koyo.ui.login.LoginActivity
import spire.util.Opt

import scalaz._
import scalaz.std.option._
import Scalaz._
import scalaz.concurrent.Task

class SplashActivity extends BaseActivity {

  private val SPLASH_TIMEOUT = 2000
  private var activityStartMs: Long = _

  protected override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash)
    val cookieMan = new CookieManager()
    cookieMan.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
    CookieHandler.setDefault(cookieMan)
    activityStartMs = System.currentTimeMillis()
    credentials.fold {
      startActivityAfterTimeout(classOf[LoginActivity])
    } {
      case (username, password) =>
        Login.login(username, password).runAsync(res => runOnMainThread(() => parseLoginResult(res)))
    }
  }

  private def startActivityAfterTimeout(clazz: Class[_ <: Activity]): Unit = {
    val currentTimeMs = System.currentTimeMillis()
    val dt = currentTimeMs - activityStartMs
    if (dt < SPLASH_TIMEOUT) {
      new Handler().postDelayed(() => openActivity(clazz), dt)
    } else {
      openActivity(clazz)
    }
  }

  private def parseLoginResult(result: Throwable \/ Login.LoginStatus): Unit = {
    JobSearch.searchForJobs(SearchParams(Seq(""), "", "", JobSearch.Approved, JobSearch.Coop, "", "", Set()), "", "").runAsync(println)
    result match {
      case -\/(ex) =>
        snackbar("There was a problem logging in")
        startActivityAfterTimeout(classOf[LoginActivity])
      case \/-(res) =>
        res match {
          case Login.LoggedIn =>
            snackbar("Logged in!")
            startActivityAfterTimeout(classOf[MainActivity])
          case Login.LoggedOut =>
            snackbar("There was a problem logging in")
            startActivityAfterTimeout(classOf[LoginActivity])
          case Login.Offline =>
            snackbar("Jobmine is offline! Please try again later.")
            startActivityAfterTimeout(classOf[LoginActivity])
        }
    }
  }

  private def credentials: Opt[(String, String)] = {
    for {
      username <- Opt(Preferences.getString(Keys.USERNAME, null))
      password <- Opt(Preferences.getString(Keys.PASSWORD, null))
    } yield (username, password)
  }

}
