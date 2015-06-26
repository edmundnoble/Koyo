package io.evolutionary.koyo.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.{LinearLayoutManager, RecyclerView}
import android.util.Log
import android.view.{View, LayoutInflater, ViewGroup}
import com.squareup.okhttp.OkHttpClient
import io.evolutionary.koyo.R
import io.evolutionary.koyo._
import io.evolutionary.koyo.parsing.{ApplicationsPage, Models}

import scalaz._
import Scalaz._
import scalaz.std.option._

object ApplicationsFragment {
  def apply(apps: Seq[Models.Application]) = {
    val frag = new ApplicationsFragment()
    frag.applications = apps
  }
}

class ApplicationsFragment extends BaseFragment {

  var _applications = Seq.empty[Models.Application]
  var recyclerView: RecyclerView = _
  var adapter: ApplicationsAdapter = _
  implicit var okHttpClient: OkHttpClient = _

  override def onCreateView(inflater: LayoutInflater,
                            container: ViewGroup,
                            savedInstanceState: Bundle): View = {
    super.onCreate(savedInstanceState)
    val view = inflater.inflate(R.layout.fragment_applications, container, false)
    recyclerView = view.findViewById(R.id.applications).asInstanceOf[RecyclerView]
    val layoutManager = new LinearLayoutManager(getActivity)
    recyclerView.setLayoutManager(layoutManager)
    adapter = new ApplicationsAdapter(_applications)
    recyclerView.setAdapter(adapter)
    view
  }

  override def onActivityCreated(savedInstanceState: Bundle): Unit = {
    super.onActivityCreated(savedInstanceState)
    okHttpClient = Jobmine.makeUnsafeClient()
    adapter.applications = _applications
    Jobmine.buildTablePageViews(ApplicationsPage).runAsyncUi(parseActivityData)
  }

  private def parseActivityData(res: Throwable \/ Seq[Models.Application]): Unit = {
    res.fold({
      error: Throwable =>
        toast("There was an error fetching activity data!")
        Log.d("ApplicationsFragment", s"Error fetching application data: ${error.allInfo}}")
    }, {
      apps: Seq[Models.Application] =>
        Log.d("ApplicationsFragment", s"Got application data!")
        Log.v("ApplicationsFragment", s"App data: $apps")
        adapter.applications = apps
    })
  }

  def applications = _applications

  def applications_=(apps: Seq[Models.Application]): Unit = {
    _applications = apps
    adapter.applications = apps
  }

}