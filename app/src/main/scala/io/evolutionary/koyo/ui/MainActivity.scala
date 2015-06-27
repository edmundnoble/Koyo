package io.evolutionary.koyo.ui

import android.support.design.widget.NavigationView
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener
import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.MenuItem
import io.evolutionary.koyo.JobSearch.SearchParams
import io.evolutionary.koyo.{JobSearch, R}
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import io.evolutionary.koyo.R
import io.evolutionary.koyo.ui.applications.ApplicationsFragment
import io.evolutionary.koyo.ui.common.BaseActivity

class MainActivity extends BaseActivity with NavigationView.OnNavigationItemSelectedListener {

  private lazy val applicationsFragment = new ApplicationsFragment
  private lazy val fragmentManager = getSupportFragmentManager
  private lazy val navigationView: NavigationView = findView(R.id.navigation_view)

  protected override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    injectToolbar()
    getSupportActionBar setTitle "Applications"
    swapFragment applicationsFragment
    navigationView setNavigationItemSelectedListener this
  }

  override def onNavigationItemSelected(menuItem: MenuItem): Boolean = {
    swapFragment(menuItem.getItemId match {
      case R.id.nav_applications => applicationsFragment
    })
    true
  }

  private def swapFragment(fragment: Fragment): Unit =
    fragmentManager beginTransaction() add(R.id.container, fragment) commit()
}
