package io.evolutionary.koyo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import com.squareup.okhttp.OkHttpClient

class BaseActivity extends AppCompatActivity {

  private lazy val toolbar = getView[Toolbar](R.id.toolbar)

  protected def getView[T <: View](id: Int): T = findViewById(id).asInstanceOf[T]

  protected def injectToolbar(): Unit = {
    setSupportActionBar(toolbar)
    getSupportActionBar.setDisplayShowTitleEnabled(false)
  }

}