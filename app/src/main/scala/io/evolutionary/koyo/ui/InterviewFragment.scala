package io.evolutionary.koyo.ui

import io.evolutionary.koyo.parsing.Models

object InterviewFragment {

}

class InterviewFragment extends ModelFragment[Models.Interview, InterviewView](
  InterviewPage <<< (_.filterKeys(InterviewNormalTable))) {

}
