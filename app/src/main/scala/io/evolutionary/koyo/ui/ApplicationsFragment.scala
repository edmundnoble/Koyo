package io.evolutionary.koyo.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.{LinearLayoutManager, RecyclerView}
import android.view.{View, LayoutInflater, ViewGroup}
import io.evolutionary.koyo.R
import io.evolutionary.koyo._
import io.evolutionary.koyo.parsing.Models

object ApplicationsFragment {
  def apply(apps: Seq[Models.Application]) = {
    val frag = new ApplicationsFragment()
    frag.applications = apps
  }
}

class ApplicationsFragment extends Fragment {

  var _applications = Seq.empty[Models.Application]
  var recyclerView: RecyclerView = _
  var adapter: ApplicationsAdapter = _

  override def onCreateView(inflater: LayoutInflater,
                            container: ViewGroup,
                            savedInstanceState: Bundle): View = {
    super.onCreate(savedInstanceState)
    val view = inflater.inflate(R.layout.fragment_applications, container, false)
    Thread.sleep(10)
    recyclerView = view.findViewById(R.id.applications).asInstanceOf[RecyclerView]
    val layoutManager = new LinearLayoutManager(getActivity)
    recyclerView.setLayoutManager(layoutManager)
    adapter = new ApplicationsAdapter(_applications)
    recyclerView.setAdapter(adapter)
    view
  }

  override def onActivityCreated(savedInstanceState: Bundle): Unit = {
    super.onActivityCreated(savedInstanceState)
    adapter.applications = _applications
  }

  def applications = _applications

  def applications_=(apps: Seq[Models.Application]): Unit = {
    _applications = apps
    adapter.applications = apps
  }

}