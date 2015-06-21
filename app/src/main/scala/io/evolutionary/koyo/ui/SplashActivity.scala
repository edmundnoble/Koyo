package io.evolutionary.koyo.ui

import android.os.{Bundle, Handler}
import io.evolutionary.koyo._

class SplashActivity extends BaseActivity {

  private val SPLASH_TIMEOUT = 2000

  protected override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash)

    new Handler().postDelayed(afterTimeout _, SPLASH_TIMEOUT)
  }

  private def afterTimeout(): Unit = {
    if (credentialsExist) openActivity(classOf[MainActivity])
    else openActivity(classOf[LoginActivity])
    finish()
  }

  private def credentialsExist: Boolean =
    Preferences.getString(Keys.USERNAME, null) != null &&
      Preferences.getString(Keys.PASSWORD, null) != null

}
