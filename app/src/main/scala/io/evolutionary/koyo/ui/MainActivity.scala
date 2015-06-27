package io.evolutionary.koyo.ui

import android.support.v4.app.Fragment
import android.os.Bundle
import io.evolutionary.koyo.R
import io.evolutionary.koyo.ui.applications.ApplicationsFragment
import io.evolutionary.koyo.ui.common.BaseActivity

class MainActivity extends BaseActivity {

  private lazy val fragmentManager = getSupportFragmentManager

  protected override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    injectToolbar()
    getSupportActionBar setTitle "Applications"
    swapFragment(new ApplicationsFragment)
  }

  private def swapFragment(fragment: Fragment): Unit =
    fragmentManager beginTransaction() add(R.id.container, fragment) commit()
}
