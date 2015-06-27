package io.evolutionary.koyo.ui.common

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.squareup.okhttp.OkHttpClient
import io.evolutionary.koyo.Jobmine

class BaseFragment extends Fragment {

  implicit def context: Context = getActivity

  protected implicit var okHttpClient: OkHttpClient = _

  protected def findView[T <: View](view: View, id: Int): T = view.findViewById(id).asInstanceOf[T]

  override def onActivityCreated(savedInstanceState: Bundle): Unit = {
    super.onActivityCreated(savedInstanceState)
    okHttpClient = Jobmine.makeUnsafeClient()
  }
}