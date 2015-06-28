package io.evolutionary.koyo.ui

import android.content.Context
import android.support.design.widget.NavigationView
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.view.{Gravity, MenuItem}
import io.evolutionary.koyo.JobSearch.SearchParams
import io.evolutionary.koyo.parsing.{InterviewsPage, ApplicationsPage}
import io.evolutionary.koyo.ui.interviews.{InterviewView}
import io.evolutionary.koyo.{JobSearch, R}
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import io.evolutionary.koyo.R
import io.evolutionary.koyo.ui.applications.{ApplicationView, ApplicationsFragment}
import io.evolutionary.koyo.ui.common.BaseActivity
import io.evolutionary.koyo.ui.interviews.InterviewFragment
import spire.util.Opt

class MainActivity extends BaseActivity with NavigationView.OnNavigationItemSelectedListener {

  //private lazy val applicationsFragment = new ApplicationsFragment
  private var currentPage = R.id.nav_applications
  private lazy val interviewFragment = InterviewFragment.makeFragment
  private lazy val applicationsFragment = new ApplicationsFragment
  private lazy val fragmentManager = getSupportFragmentManager
  private lazy val drawerLayout: DrawerLayout = findView(R.id.drawer_layout)
  private lazy val navigationView: NavigationView = findView(R.id.navigation_view)

  protected override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    injectToolbar()
    getSupportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
    getSupportActionBar.setDisplayHomeAsUpEnabled(true)
    setToolbarTitle("Applications")
    swapFragment(applicationsFragment)
    navigationView.setNavigationItemSelectedListener(this)
  }

  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    if (item.getItemId == android.R.id.home) {
      drawerLayout.openDrawer(Gravity.LEFT)
      return true
    }
    super.onOptionsItemSelected(item)
  }

  override def onNavigationItemSelected(menuItem: MenuItem): Boolean = {
    drawerLayout.closeDrawer(Gravity.LEFT)
    val selectedPage = menuItem.getItemId
    if (selectedPage != currentPage) {
      swapFragment {
        selectedPage match {
          case R.id.nav_interviews =>
            setToolbarTitle("Interviews")
            interviewFragment
          case R.id.nav_applications =>
            setToolbarTitle("Applications")
            applicationsFragment
        }
      }
      menuItem.setChecked(!menuItem.isChecked)
    }
    currentPage = selectedPage
    true
  }

  private def deleteFragment(fragment: Fragment): Unit = {
    fragmentManager.beginTransaction().remove(fragment).commit()
  }

  private def swapFragment(fragment: Fragment): Unit =
    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
}
