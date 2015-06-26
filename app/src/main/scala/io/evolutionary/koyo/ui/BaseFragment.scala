package io.evolutionary.koyo.ui

import android.content.Context
import android.support.v4.app.Fragment

class BaseFragment extends Fragment {
  implicit def context: Context = getActivity
}