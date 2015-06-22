package io.evolutionary.koyo.ui

import android.content.{Context, Intent}
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import io.evolutionary.koyo.{Jobmine, R}
import com.squareup.okhttp.OkHttpClient

class BaseActivity extends AppCompatActivity {

  protected implicit val thisContext: Context = this
  protected implicit var okHttpClient: OkHttpClient = _

  protected override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    okHttpClient = Jobmine.makeUnsafeClient()
  }

  def openActivity(destination: Class[_]): Unit =
    startActivity(new Intent(this, destination))

  private lazy val toolbar = getView[Toolbar](R.id.toolbar)

  protected def getView[T <: View](id: Int): T = findViewById(id).asInstanceOf[T]

  protected def injectToolbar(): Unit = {
    setSupportActionBar(toolbar)
    getSupportActionBar.setDisplayShowTitleEnabled(false)
  }

}
