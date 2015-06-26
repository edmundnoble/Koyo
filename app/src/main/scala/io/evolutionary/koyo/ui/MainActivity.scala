package io.evolutionary.koyo.ui

import android.content.Context
import android.support.v4.app.{FragmentManager, Fragment}
import android.os.Bundle
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout
import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout.TabColorizer
import io.evolutionary.koyo.R
import io.evolutionary.koyo.parsing.{ActiveApplicationsTable, AllApplicationsTable, ApplicationsPage, Models}

class MainActivity extends BaseActivity {
  lazy val viewPager: ViewPager = getView(R.id.pager)
  lazy val tabLayout: SlidingTabLayout = getView(R.id.layout)

  protected override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

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
  val makeApplicationView = (context: Context, model: Models.Application) => new ApplicationView(context, model)
  val allAppPage = ApplicationsPage <<< (_.filterKeys(_ == AllApplicationsTable))
  val activeAppPage = ApplicationsPage <<< (_.filterKeys(_ == ActiveApplicationsTable))

  val FragmentsInOrder: Seq[() => Fragment] = Seq(
    () => new ModelFragment(allAppPage >>> (_.sortBy (_.employer)), makeApplicationView),
    () => new ModelFragment(activeAppPage >>> (_.sortBy(_.employer)), makeApplicationView)
  )
  val FragmentTitlesInOrder: Seq[String] = Seq("All Applications", "Active Applications")
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
