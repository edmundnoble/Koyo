package io.evolutionary.koyo

import android.app.Activity
import android.content.{Context, Intent}
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast
import com.squareup.okhttp.OkHttpClient

class BaseActivity extends AppCompatActivity {

  implicit val thisContext: Context = this

  def openActivity(destination: Class[_]): Unit =
    startActivity(new Intent(this, destination))

  private lazy val toolbar = getView[Toolbar](R.id.toolbar)

  protected def getView[T <: View](id: Int): T = findViewById(id).asInstanceOf[T]

  protected def injectToolbar(): Unit = {
    setSupportActionBar(toolbar)
    getSupportActionBar.setDisplayShowTitleEnabled(false)
  }

}