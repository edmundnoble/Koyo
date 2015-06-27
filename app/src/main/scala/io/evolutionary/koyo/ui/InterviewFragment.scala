package io.evolutionary.koyo.ui

import io.evolutionary.koyo.parsing.Models
import io.evolutionary.koyo.ui.interviews.InterviewView

object InterviewFragment {
  def makeFragment = new InterviewFragment
}

class InterviewFragment extends ModelListFragment[Models.Interview, InterviewView](InterviewPage.before(_.filterKeys(InterviewNormalTable))) {

}
