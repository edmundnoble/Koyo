package io.evolutionary.koyo.ui.login

import java.io.InterruptedIOException
import java.net.UnknownHostException

import android.app.ProgressDialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.widget.EditText
import io.evolutionary.koyo.JobSearch.SearchParams
import io.evolutionary.koyo.Login._
import io.evolutionary.koyo._
import io.evolutionary.koyo.ui.MainActivity
import io.evolutionary.koyo.ui.common.BaseActivity

import scalaz.{-\/, \/-, \/}

class LoginActivity extends BaseActivity {

  private var loggingIn = false
  private lazy val progressDialog = new ProgressDialog(this)
  private lazy val usernameField = findView[EditText](R.id.usernameField)
  private lazy val passwordField = findView[EditText](R.id.passwordField)

  protected override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)
    injectToolbar()
    enableToolbarLogo(true)
    progressDialog.setMessage("Loading")
  }

  def onLoginButtonClicked(view: View): Unit = {
    if (!loggingIn) {
      val username = usernameField.getString
      val password = passwordField.getString
      loggingIn = true
      progressDialog.show()
      Login.login(username, password).runAsyncUi(parseLoginStatus(username, password, _))
    }
  }

  private def parseLoginStatus(username: String, password: String, statusOrErr: Throwable \/ LoginStatus): Unit = {
    progressDialog.dismiss()
    statusOrErr match {
      case \/-(status) => status match {
        case LoggedIn =>
          snackbar("You're logged in now!")
          saveCredentials(username, password)
          openActivity(classOf[MainActivity])
          finish()
        case LoggedOut => snackbar("Your credentials are invalid!")
        case Offline => snackbar("Jobmine is offline! Please try again later.")
      }
      case -\/(err) => err match {
        case ex: UnknownHostException => snackbar("No internet connection!")
        case _: InterruptedIOException => snackbar("Login timed out")
        case e: Exception => Log.e("LoginActivity", s"Error logging in: \n${err.allInfo}")
      }
    }
    loggingIn = false
  }

  private def saveCredentials(username: String, password: String): Unit = {
    Preferences.putString(Keys.USERNAME, username)
    Preferences.putString(Keys.PASSWORD, password)
  }

}
