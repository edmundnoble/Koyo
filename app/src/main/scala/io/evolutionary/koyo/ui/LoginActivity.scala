package io.evolutionary.koyo.ui

import java.io.InterruptedIOException
import java.net.UnknownHostException

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import com.squareup.okhttp.OkHttpClient
import io.evolutionary.koyo.Login._
import io.evolutionary.koyo._
import io.evolutionary.koyo.parsing.{ApplicationsPage, InterviewsPage, TableHeaders}

import scalaz._
import scalaz.concurrent.Task

class LoginActivity extends BaseActivity {

  private var loggingIn = false
  private lazy val progressDialog = new ProgressDialog(this)
  private lazy val usernameField = getView[EditText](R.id.usernameField)
  private lazy val passwordField = getView[EditText](R.id.passwordField)

  protected override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)
    injectToolbar()
    progressDialog.setMessage("Loading")
  }

  def onLoginButtonClicked(view: View): Unit = {
    if (!loggingIn) {
      val username = usernameField.getString
      val password = passwordField.getString
      Login.login(username, password).runAsyncUi(parseLoginStatus(username, password, _))
      loggingIn = true
      progressDialog.show()
    }
  }

  private def parseLoginStatus(username: String, password: String, statusOrErr: Throwable \/ LoginStatus): Unit = {
    progressDialog.dismiss()
    statusOrErr match {
      case \/-(status) => status match {
        case LoggedIn =>
          toast("You're logged in now!")
          saveCredentials(username, password)
          openActivity(classOf[MainActivity])
          finish()
        case LoggedOut => toast("Your credentials are invalid!")
        case Offline => toast("Jobmine is offline! Please try again later.")
      }
      case -\/(err) => err match {
        case ex: UnknownHostException => toast("No internet connection!")
        case _: InterruptedIOException => toast("Login timed out")
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
