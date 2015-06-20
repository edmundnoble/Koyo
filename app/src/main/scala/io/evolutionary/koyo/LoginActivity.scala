package io.evolutionary.koyo

import java.net.UnknownHostException

import android.support.v7.app.AppCompatActivity

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.{View, Menu, MenuItem}
import android.widget.{EditText, Toast, Button, TextView}
import com.squareup.okhttp.OkHttpClient
import io.evolutionary.koyo.Login._
import scalaz._
import scalaz.concurrent.{Task, Promise}

class LoginActivity extends BaseActivity {

  private implicit var httpClient: OkHttpClient = _
  private lazy val usernameField = getView[EditText](R.id.usernameField)
  private lazy val passwordField = getView[EditText](R.id.passwordField)

  protected override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)
    injectToolbar()

    httpClient = Jobmine.makeUnsafeClient()
  }

  def onLoginButtonClicked(view: View): Unit =
    Login.login(usernameField.getString, passwordField.getString).runAsync(parseLoginStatus)

  private def parseLoginStatus(statusOrErr: Throwable \/ LoginStatus): Unit = runOnUiThread {
    statusOrErr match {
      case \/-(status) => status match {
        case LoggedIn => toast("You're logged in now!")
        case LoggedOut => toast("Your credentials are invalid!")
        case Offline => toast("Jobmine is offline! Please try again later.")
      }
      case -\/(err) => err match {
        case ex: UnknownHostException => toast("No internet connection!")
        case e: Exception => Log.e("LoginActivity", s"Error logging in: \n${err.stackTraceAsString}")
      }
    }
  }

}
