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

import scala.reflect.ClassTag
import scalaz.{\/-, -\/, \/}

class SplashActivity extends BaseActivity {

  private val SPLASH_TIMEOUT = 2000
  private var activityStartMs: Long = _

  protected override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    activityStartMs = System.currentTimeMillis()
    setContentView(R.layout.activity_splash)
    val cookieMan = new CookieManager()
    cookieMan.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
    CookieHandler.setDefault(cookieMan)
    credentials.fold {
      startActivityAfterTimeout[LoginActivity]()
    } {
      case (username, password) =>
        Login.login(username, password).runAsync(res => runOnMainThread(() => parseLoginResult(res)))
    }
  }

  private def startActivityAfterTimeout[T <: Activity]()(implicit tag: ClassTag[T]): Unit = {
    val clazz = tag.runtimeClass
    val currentTimeMs = System.currentTimeMillis()
    val dt = currentTimeMs - activityStartMs
    if (dt < SPLASH_TIMEOUT) {
      new Handler().postDelayed(() => openActivity(clazz), math.max(SPLASH_TIMEOUT - dt, 0))
    } else {
      openActivity(clazz)
    }
  }

  private def parseLoginResult(result: Throwable \/ Login.LoginStatus): Unit = {
    result match {
      case -\/(ex) =>
        snackbar("There was a problem logging in")
        startActivityAfterTimeout[LoginActivity]()
      case \/-(res) =>
        res match {
          case Login.LoggedIn =>
            snackbar("Logged in!")
            startActivityAfterTimeout[MainActivity]()
          case Login.LoggedOut =>
            snackbar("There was a problem logging in")
            startActivityAfterTimeout[LoginActivity]()
          case Login.Offline =>
            snackbar("Jobmine is offline! Please try again later.")
            startActivityAfterTimeout[LoginActivity]()
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
