package io.evolutionary.koyo.ui

import java.net.{CookieHandler, CookiePolicy, CookieManager}

import android.app.Activity
import android.os.{Bundle, Handler}
import com.squareup.okhttp.OkHttpClient
import io.evolutionary.koyo._

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
    credentials.cata({
      case (username, password) =>
        Login.login(username, password).runAsync(res => runOnMainThread(() => parseLoginResult(res)))
    }, {
      startActivityAfterTimeout(classOf[LoginActivity])
    })
  }

  private def startActivityAfterTimeout(clazz: Class[_ <: Activity]) = {
    val currentTimeMs = System.currentTimeMillis()
    val dt = currentTimeMs - activityStartMs
    if (dt < SPLASH_TIMEOUT) {
      new Handler() postDelayed(() => openActivity(clazz), dt)
    } else {
      openActivity(clazz)
    }
  }

  private def parseLoginResult(result: Throwable \/ Login.LoginStatus): Unit = {
    result match {
      case -\/(ex) =>
        toast("There was a problem logging in")
        startActivityAfterTimeout(classOf[LoginActivity])
      case \/-(res) =>
        res match {
          case Login.LoggedIn =>
            toast("Logged in!")
            startActivityAfterTimeout(classOf[MainActivity])
          case Login.LoggedOut =>
            toast("There was a problem logging in")
            startActivityAfterTimeout(classOf[LoginActivity])
          case Login.Offline =>
            toast("Jobmine is offline! Please try again later.")
            startActivityAfterTimeout(classOf[LoginActivity])
        }
    }
  }

  private def credentials: Option[(String, String)] = {
    for {
      username <- Option(Preferences.getString(Keys.USERNAME, null))
      password <- Option(Preferences.getString(Keys.PASSWORD, null))
    } yield (username, password)
  }

}
