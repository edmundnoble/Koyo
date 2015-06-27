package io.evolutionary.koyo.ui.applications

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.{Fragment, FragmentPagerAdapter, FragmentManager}
import android.support.v4.view.ViewPager
import android.view.{View, ViewGroup, LayoutInflater}
import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout
import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout.TabColorizer
import io.evolutionary.koyo.parsing.{AllApplicationsTable, ActiveApplicationsTable, ApplicationsPage, Models}
import io.evolutionary.koyo.ui.{ModelListFragment, ModelAdapter}
import io.evolutionary.koyo.{Jobmine, R}
import io.evolutionary.koyo.ui.common.BaseFragment

class ApplicationsFragment extends BaseFragment {

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    super.onCreate(savedInstanceState)
    val view = inflater.inflate(R.layout.fragment_applications, container, false)
    val viewPager: ViewPager = findView(view, R.id.pager)
    val tabLayout: TabLayout = findView(view, R.id.tab_layout)

    def getColor(id: Int): Int = getResources.getColor(id)
    viewPager.setAdapter(new MainPagerAdapter(getActivity.getSupportFragmentManager))
    tabLayout.setTabTextColors(getColor(R.color.tab_text_inactive), getColor(R.color.tab_text_active))
    tabLayout.setupWithViewPager(viewPager)
    view
  }
}

object MainPagerAdapter {
  val makeApplicationView = (context: Context) => new ApplicationView(context)
  val FragmentsInOrder: Seq[() => Fragment] = Seq(
    () => new ModelListFragment(ApplicationsPage <<< (_.filterKeys(_ == ActiveApplicationsTable)) >>> (_.sortBy(_.employer)), makeApplicationView),
    () => new ModelListFragment(ApplicationsPage <<< (_.filterKeys(_ == AllApplicationsTable)) >>> (_.sortBy(_.employer)), makeApplicationView)
  )
  val FragmentTitlesInOrder: Seq[String] = Seq("Active", "All")
}

class MainPagerAdapter(fm: FragmentManager) extends FragmentPagerAdapter(fm) {

  import MainPagerAdapter._

  override def getItem(position: Int): Fragment = FragmentsInOrder(position)()

  override def getPageTitle(position: Int): CharSequence = FragmentTitlesInOrder(position)

  override def getCount: Int = FragmentsInOrder.length
}

