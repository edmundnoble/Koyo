package io.evolutionary.koyo.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.{View, LayoutInflater, ViewGroup}
import android.widget.{ProgressBar, ListView}
import com.squareup.okhttp.OkHttpClient
import io.evolutionary.koyo.R
import io.evolutionary.koyo._
import io.evolutionary.koyo.parsing.{InterviewsPage, TablePage, ApplicationsPage, Models}
import io.evolutionary.koyo.ui.common.BaseFragment

import scalaz._
import Scalaz._
import scalaz.std.option._

class ModelListFragment[Model, V <: View with ModelView[Model]](page: TablePage[Model, _], makeView: (Context) => V) extends BaseFragment {

  private var progress: ProgressBar = _
  private var errorContainer: ViewGroup = _
  private var listView: ListView = _
  private var adapter: ModelAdapter[Model, V] = _

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    super.onCreate(savedInstanceState)
    val view = inflater.inflate(R.layout.fragment_model, container, false)
    progress = view.findView(R.id.progress)
    errorContainer = view.findView(R.id.error_container)
    listView = view.findView(R.id.model_list_view)
    if (adapter != null) {
      val models = adapter.models
      adapter = new ModelAdapter[Model, V](getActivity, makeView)
      adapter.models = models
      hide(progress)
      listView.setAdapter(adapter)
      show(listView)
    } else {
      adapter = new ModelAdapter[Model, V](getActivity, makeView)
      Jobmine.requestTablePageRows(page).runAsyncUi(parseModelData)
    }
    view
  }

  private def parseModelData(res: Throwable \/ Seq[Model]): Unit = {
    hide(progress)
    res.fold({
      error: Throwable =>
        snackbar("There was an error fetching activity data!")(getActivity)
        Log.d("ApplicationsFragment", s"Error fetching application data: ${error.allInfo}}")
        show(errorContainer)
    }, {
      models: Seq[Model] =>
        adapter.models = models
        listView.setAdapter(adapter)
        show(listView)
    })
  }

}
