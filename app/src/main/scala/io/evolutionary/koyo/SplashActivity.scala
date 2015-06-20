package io.evolutionary.koyo

import android.os.{Handler, Bundle}
import android.preference.PreferenceManager

class SplashActivity extends BaseActivity {

  private val SPLASH_TIMEOUT = 1500
  private lazy val preferences = PreferenceManager.getDefaultSharedPreferences(this)

  protected override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash)

    new Handler() postDelayed(toRunnable(afterTimeout()), SPLASH_TIMEOUT)
  }

  private def afterTimeout(): Unit = {
    if (credentialsExist) openActivity(this, classOf[MainActivity])
    else openActivity(this, classOf[LoginActivity])
    finish()
  }

  private def credentialsExist: Boolean =
    (preferences.getString(Keys.USERNAME, null) != null) &&
      (preferences.getString(Keys.PASSWORD, null) != null)

}
