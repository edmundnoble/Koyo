package io.evolutionary.koyo

import android.support.v7.app.AppCompatActivity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

class LoginActivity extends AppCompatActivity {

  implicit var httpClient = _

  protected override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)
    httpClient = Jobmine.makeUnsafeClient
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater.inflate(R.menu.menu_login, menu)
    true
  }

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
