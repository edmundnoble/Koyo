package io.evolutionary.koyo.ui

import android.os.Bundle
import io.evolutionary.koyo.R

class MainActivity extends BaseActivity {

  protected override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

  }
}
