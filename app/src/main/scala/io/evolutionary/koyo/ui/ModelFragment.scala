package io.evolutionary.koyo.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.{View, LayoutInflater, ViewGroup}
import android.widget.ListView
import com.squareup.okhttp.OkHttpClient
import io.evolutionary.koyo.R
import io.evolutionary.koyo._
import io.evolutionary.koyo.parsing.{TablePage, ApplicationsPage, Models}

import scalaz._
import Scalaz._
import scalaz.std.option._

class ModelFragment[Model, V <: View with ModelView[Model]](page: TablePage[Model, _], makeView: (Context, Model) => V) extends BaseFragment {
  var listView: ListView = _
  var adapter: ModelAdapter[Model, V] = _
  implicit var okHttpClient: OkHttpClient = _

  override def onCreateView(inflater: LayoutInflater,
                            container: ViewGroup,
                            savedInstanceState: Bundle): View = {
    super.onCreate(savedInstanceState)
    val view = inflater.inflate(R.layout.fragment_model, container, false)
    listView = view.getView(R.id.applications)
    view
  }

  override def onActivityCreated(savedInstanceState: Bundle): Unit = {
    super.onActivityCreated(savedInstanceState)
    okHttpClient = Jobmine.makeUnsafeClient()
    adapter = new ModelAdapter[Model, V](getActivity, makeView)
    listView.setAdapter(adapter)
    Jobmine.requestTablePageRows(page).runAsyncUi(parseActivityData)
  }

  private def parseActivityData(res: Throwable \/ Seq[Model]): Unit = {
    res.fold({
      error: Throwable =>
        toast("There was an error fetching activity data!")
        Log.d("ApplicationsFragment", s"Error fetching application data: ${error.allInfo}}")
    }, {
      models: Seq[Model] =>
        adapter.models = models
    })
  }

}
