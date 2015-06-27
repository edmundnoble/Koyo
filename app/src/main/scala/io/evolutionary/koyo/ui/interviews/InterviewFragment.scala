package io.evolutionary.koyo.ui.interviews

import android.content.Context
import io.evolutionary.koyo.parsing.{InterviewNormal, Models, InterviewsPage}
import io.evolutionary.koyo.ui.ModelListFragment

object InterviewFragment {
  def makeFragment = new ModelListFragment(InterviewsPage <<< (_.filterKeys(_ == InterviewNormal)), (context: Context) => new InterviewView(context))
}
