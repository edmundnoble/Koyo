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

class LoginActivity extends AppCompatActivity {

  implicit var httpClient: OkHttpClient = _
  lazy val toolbar = getView[Toolbar](R.id.toolbar)
  lazy val usernameField = getView[EditText](R.id.usernameField)
  lazy val passwordField = getView[EditText](R.id.passwordField)

  private def getView[T <: View](id: Int): T = findViewById(id).asInstanceOf[T]

  protected override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)
    setSupportActionBar(toolbar)
    getSupportActionBar.setDisplayShowTitleEnabled(false)
    httpClient = Jobmine.makeUnsafeClient()
  }

  def onLoginButtonClicked(view: View): Unit = {
    Login.login(usernameField.getString, passwordField.getString).runAsync(parseLoginStatus)
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater.inflate(R.menu.menu_login, menu)
    true
  }

  private def parseLoginStatus(statusOrErr: Throwable \/ LoginStatus): Unit = runOnUiThread {
    statusOrErr match {
      case \/-(status) => status match {
        case LoggedIn =>
          Toast.makeText(this, "You're logged in now!", Toast.LENGTH_SHORT).show()
        case LoggedOut =>
          Toast.makeText(this, "Your credentials are shite!", Toast.LENGTH_SHORT).show()
        case Offline =>
          Toast.makeText(this, "Jobmine is offline! Please try again later.", Toast.LENGTH_SHORT).show()
      }
      case -\/(err) => err match {
        case ex: UnknownHostException =>
          Toast.makeText(this, "No internet connection!", Toast.LENGTH_SHORT).show()
        case e: Exception =>
          Log.e("LoginActivity", s"Error logging in: \n${err.stackTraceAsString}")
      }
    }
  }

  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    val id = item.getItemId

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings)
      true
    else
      super.onOptionsItemSelected(item)
  }
}
