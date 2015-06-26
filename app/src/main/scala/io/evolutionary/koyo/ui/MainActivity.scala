package io.evolutionary.koyo.ui

import android.os.Bundle
import android.support.v4.app.{Fragment, FragmentPagerAdapter, FragmentManager}
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout
import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout.TabColorizer
import io.evolutionary.koyo.R
import io.evolutionary.koyo.parsing.{ApplicationsPage, Models}

class MainActivity extends BaseActivity {
  lazy val viewPager: ViewPager = getView(R.id.pager)
  lazy val tabLayout: SlidingTabLayout = getView(R.id.layout)

  protected override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    toolbar.setElevation(0)

    injectToolbar()
    viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager))
    tabLayout.setDistributeEvenly(true)
    tabLayout.setCustomTabView(R.layout.tab_layout, android.R.id.text1)
    tabLayout.setCustomTabColorizer(new TabColorizer {
      override def getIndicatorColor(position: Int): Int = getResources.getColor(android.R.color.white)
    })
    tabLayout.setViewPager(viewPager)
  }
}

object MainPagerAdapter {
  val FragmentsInOrder: Seq[() => Fragment] = Seq(
    () => new ModelFragment[Models.Application, ApplicationView](ApplicationsPage, (context, model) => new ApplicationView(context, model))
  )
  val FragmentTitlesInOrder: Seq[String] = Seq("Applications")
}

class MainPagerAdapter(fm: FragmentManager) extends FragmentPagerAdapter(fm) {

  import MainPagerAdapter._

  override def getItem(position: Int): Fragment = {
    FragmentsInOrder(position)()
  }

  override def getPageTitle(position: Int): CharSequence = {
    FragmentTitlesInOrder(position)
  }

  override def getCount: Int = FragmentsInOrder.length
}
