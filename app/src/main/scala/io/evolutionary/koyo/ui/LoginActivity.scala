package io.evolutionary.koyo.ui

import java.net.UnknownHostException

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import com.squareup.okhttp.OkHttpClient
import io.evolutionary.koyo.Login._
import io.evolutionary.koyo._

import scalaz._

class LoginActivity extends BaseActivity {

  private implicit var httpClient: OkHttpClient = _
  private var username: String = null
  private var password: String = null
  private lazy val usernameField = getView[EditText](R.id.usernameField)
  private lazy val passwordField = getView[EditText](R.id.passwordField)

  protected override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)
    injectToolbar()

    httpClient = Jobmine.makeUnsafeClient()
  }

  def onLoginButtonClicked(view: View): Unit = {
    username = usernameField.getString
    password = passwordField.getString
    Login.login(username, password).runAsync(parseLoginStatus)
  }

  private def parseLoginStatus(statusOrErr: Throwable \/ LoginStatus): Unit = runOnUiThread {
    statusOrErr match {
      case \/-(status) => status match {
        case LoggedIn => {
          toast("You're logged in now!")
          saveCredentials()
          openActivity(classOf[MainActivity])
          finish()
        }
        case LoggedOut => toast("Your credentials are invalid!")
        case Offline => toast("Jobmine is offline! Please try again later.")
      }
      case -\/(err) => err match {
        case ex: UnknownHostException => toast("No internet connection!")
        case e: Exception => Log.e("LoginActivity", s"Error logging in: \n${err.stackTraceAsString}")
      }
    }
  }

  private def saveCredentials(): Unit = {
    Preferences.putString(Keys.USERNAME, username)
    Preferences.putString(Keys.PASSWORD, password)
  }

}
