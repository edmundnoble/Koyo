package io.evolutionary.koyo.ui.common

import android.app.Activity
import android.content.{Context, Intent}
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import com.squareup.okhttp.OkHttpClient
import io.evolutionary.koyo.{Jobmine, R}

class BaseActivity extends AppCompatActivity {

  protected implicit val thisContext: Context = this
  protected implicit val thisActivity: Activity = this
  protected implicit var okHttpClient: OkHttpClient = _

  protected override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    okHttpClient = Jobmine.makeUnsafeClient()
  }

  def openActivity(destination: Class[_]): Unit =
    startActivity(new Intent(this, destination))

  protected def findView[T <: View](id: Int): T = findViewById(id).asInstanceOf[T]

  protected lazy val toolbar = findView[Toolbar](R.id.toolbar)

  protected def injectToolbar(): Unit = {
    setSupportActionBar(toolbar)
  }

  protected def enableToolbarLogo(enable: Boolean): Unit = {
    toolbar.findViewById(R.id.logo).setVisibility(if (enable) View.VISIBLE else View.GONE)
    getSupportActionBar.setDisplayShowTitleEnabled(false)
  }

  protected def setToolbarTitle(title: String): Unit = getSupportActionBar setTitle title

}