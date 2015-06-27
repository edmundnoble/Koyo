package io.evolutionary

import java.util
import java.util.Date
import java.util.concurrent._

import android.app.Activity
import android.content.{Intent, Context}
import android.os.{AsyncTask, Looper, Handler}
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.{Toast, EditText, TextView}
import com.squareup.okhttp.{ResponseBody, RequestBody, Response}
import io.evolutionary.koyo.parsing.HtmlParser
import org.jsoup.Jsoup
import spire.util.Opt

import scala.concurrent.ExecutionContext
import scala.language.implicitConversions
import scala.util.Try
import scalaz.concurrent.{Task, Strategy}
import scalaz._
import Scalaz._
import java.io.{PrintWriter, StringWriter}

package object koyo {
  type OkCallback = (Throwable \/ Response) => Unit

  implicit def toRunnable[T](block: () => T): Runnable = new Runnable {
    override def run(): Unit = block()
  }


  implicit def toOnClickListener[T](block: => T): OnClickListener = new OnClickListener {
    override def onClick(view: View): Unit = block
  }

  def ignore[A](a: A): Unit = ()

  implicit class EditTextWithString(val self: EditText) extends AnyVal {
    def getString = self.getText.toString
  }

  def runOnMainThread[T](block: () => T): Unit = {
    println("Trying to run something on the main thread...")
    if (!onMainThread)
      new Handler(Looper.getMainLooper) post block
    else
      block()
  }

  def snackbar(text: String)(implicit activity: Activity): Unit =
    Snackbar.make(activity.getWindow.getDecorView.findView(android.R.id.content), text, Snackbar.LENGTH_SHORT).show()

  implicit class FindViewable(val v: View) extends AnyVal {
    def findView[T <: View](id: Int): T = v.findViewById(id).asInstanceOf[T]
  }

  def onMainThread: Boolean =
    Looper.myLooper eq Looper.getMainLooper

  implicit class TaskThreadTools[T](val t: Task[T]) extends AnyVal {
    def ui: Task[Unit] = Task.delay(runOnMainThread(() => t.run))

    def runAsyncUi(fun: (Throwable \/ T) => Unit): Unit = {
      t.runAsync((res) => runOnMainThread(() => fun(res)))
    }

    def bg: Task[T] = Task.async[T] { cb =>
      if (!onMainThread) {
        cb(t.attemptRun)
      } else {
        val task = new AsyncTask[AnyRef, Void, AnyRef] {
          // This looks weird, doesn't it? Thanks to SI-1459 (and maybe another bug),
          // everything here has to be AnyRef.
          override def doInBackground(paramses: AnyRef*): AnyRef = {
            Log.d("Package", "Doing something in the background...")
            cb(t.attemptRun)
            null
          }

          override def onCancelled(): Unit = cb(new RuntimeException().left[T])
        }.execute()
      }
    }
  }

  implicit class ResponseBodyWithHtml(val body: ResponseBody) {
    lazy val html = Jsoup.parse(body.string())
  }

  implicit class ExceptionWithAllInfo(val ex: Throwable) extends AnyVal {
    def allInfo: String = {
      val sw = new StringWriter()
      ex.printStackTrace(new PrintWriter(sw))
      sw.toString
    }
  }

  implicit class OptionalStringParsers(val str: String) extends AnyVal {
    def parseInt: Option[Int] = Try(str.toInt).toOption

    def parseBool: Option[Boolean] = Try(str.toBoolean).toOption
  }

}
