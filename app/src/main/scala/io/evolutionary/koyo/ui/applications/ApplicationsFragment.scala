package io.evolutionary.koyo.ui.applications

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.{FragmentStatePagerAdapter, Fragment, FragmentPagerAdapter, FragmentManager}
import android.support.v4.view.ViewPager
import android.view.{View, ViewGroup, LayoutInflater}
import io.evolutionary.koyo.parsing.{AllApplicationsTable, ActiveApplicationsTable, ApplicationsPage, Models}
import io.evolutionary.koyo.ui.{ModelListFragment, ModelAdapter}
import io.evolutionary.koyo._
import io.evolutionary.koyo.ui.common.BaseFragment

import scalaz.Need

class ApplicationsFragment extends BaseFragment {

  var viewPager: ViewPager = _
  var tabLayout: TabLayout = _

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    super.onCreate(savedInstanceState)
    val view = inflater.inflate(R.layout.fragment_applications, container, false)
    viewPager = view.findView(R.id.pager)
    tabLayout = view.findView(R.id.tab_layout)
    view
  }

  override def onActivityCreated(savedInstanceState: Bundle): Unit = {
    super.onActivityCreated(savedInstanceState)
    def getColor(id: Int): Int = getResources.getColor(id)
    viewPager.setAdapter(new MainPagerAdapter(getChildFragmentManager))
    tabLayout.setTabTextColors(getColor(R.color.tab_text_inactive), getColor(R.color.tab_text_active))
    tabLayout.setupWithViewPager(viewPager)
  }
}

object MainPagerAdapter {
  val makeApplicationView = (context: Context) => new ApplicationView(context)

  val FragmentsInOrder: Seq[Need[Fragment]] = Seq(
    Need(new ModelListFragment(ApplicationsPage <<< (_.filterKeys(_ == ActiveApplicationsTable)) >>> (_.sortBy(_.employer)), makeApplicationView)),
    Need(new ModelListFragment(ApplicationsPage <<< (_.filterKeys(_ == AllApplicationsTable)) >>> (_.sortBy(_.employer)), makeApplicationView))
  )
  val FragmentTitlesInOrder: Seq[String] = Seq("Active", "All")
}

class MainPagerAdapter(fm: FragmentManager) extends FragmentPagerAdapter(fm) {

  import MainPagerAdapter._

  override def getItem(position: Int): Fragment = FragmentsInOrder(position).value

  override def getPageTitle(position: Int): CharSequence = FragmentTitlesInOrder(position)

  override def getCount: Int = FragmentsInOrder.length
}

