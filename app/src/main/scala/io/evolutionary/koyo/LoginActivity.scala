package io.evolutionary.koyo

import android.support.v7.app.AppCompatActivity

import android.os.Bundle
import android.util.Log
import android.view.{View, Menu, MenuItem}
import android.widget.{EditText, Toast, Button, TextView}
import com.squareup.okhttp.OkHttpClient
import io.evolutionary.koyo.Login._
import scalaz.concurrent.{Task, Promise}

class LoginActivity extends AppCompatActivity {

  implicit var httpClient: OkHttpClient = _
  lazy val usernameField = getView[EditText](R.id.usernameField)
  lazy val passwordField = getView[EditText](R.id.passwordField)
  lazy val loginButton = getView[Button](R.id.loginButton)

  private def getView[T <: View](id: Int): T = findViewById(id).asInstanceOf[T]

  protected override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)
    httpClient = Jobmine.makeUnsafeClient()

    loginButton.setOnClickListener(
      (_: View) =>
        Login.login(usernameField.getString, passwordField.getString).flatMap(parseLoginStatus).attemptRun
    )
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater.inflate(R.menu.menu_login, menu)
    true
  }

  private def parseLoginStatus(status: LoginStatus): Task[Unit] = Task.delay {
    Log.d("LoginActivity", s"Parsing login status $status")
    status match  {
      case LoggedIn =>
      case LoggedOut =>
        Toast.makeText(this, "Your credentials are shite!", Toast.LENGTH_SHORT).show()
      case Offline =>
        Toast.makeText(this, "Jobmine is offline! Please try again later.", Toast.LENGTH_SHORT).show()
    }
  }.ui

  @Override
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
