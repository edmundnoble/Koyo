package io.evolutionary

import java.util
import java.util.concurrent._

import android.app.Activity
import android.content.{Intent, Context}
import android.os.{AsyncTask, Looper, Handler}
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.{Toast, EditText, TextView}
import com.squareup.okhttp.Response

import scala.concurrent.ExecutionContext
import scala.language.implicitConversions
import scala.util.Try
import scalaz.concurrent.{Task, Strategy}
import scalaz._
import Scalaz._
import java.io.{PrintWriter, StringWriter}

package object koyo {
  type OkCallback = (Throwable \/ Response) => Unit

  implicit def toRunnable[T](block: => T): Runnable = new Runnable {
    override def run(): Unit = block
  }

  implicit def toOnClickListener[T](block: => T): OnClickListener = new OnClickListener {
    override def onClick(view: View): Unit = block
  }

  implicit class EditTextWithString(val self: EditText) extends AnyVal {
    def getString = self.getText.toString
  }

  def runOnMainThread(block: Runnable): Unit = {
    if (!onMainThread)
      new Handler(Looper.getMainLooper) post block
    else
      block.run()
  }

  def openActivity(current: Activity, destination: Class[_]): Unit =
    current.startActivity(new Intent(current, destination))

  def toast(text: String, context: Context): Unit =
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()

  def onMainThread: Boolean =
    Looper.myLooper == Looper.getMainLooper

  implicit class TaskOnMainThread[T](val t: Task[T]) extends AnyVal {
    def ui: Task[Unit] = Task.delay(runOnMainThread {
      t.run
    })
  }

  implicit def DisToTry[T](d: Throwable \/ T): Try[T] = d match {
    case -\/(ex) => Try(throw ex)
    case \/-(res) => Try(res)
  }

  implicit class TaskOnBackgroundThread[T](val t: Task[T]) extends AnyVal {
    def bg: Task[T] = Task.async[T] { cb =>
      if (!onMainThread) {
        cb(t.attemptRun)
      } else {
        val task = new AsyncTask[AnyRef, Void, AnyRef] {
          // This looks weird, doesn't it? Thanks to SI-1459 (and maybe another bug),
          // everything here has to be AnyRef.
          override def doInBackground(paramses: AnyRef*): AnyRef = {
            Log.d("Package", "Doing something in the background..."); cb(t.attemptRun); null
          }

          override def onCancelled(): Unit = cb(new RuntimeException().left[T])
        }.execute()
      }
    }
  }

  implicit val ec = new ExecutionContext() {
    override def execute(runnable: Runnable): Unit = runnable.run()

    override def reportFailure(cause: Throwable): Unit = cause.printStackTrace()
  }

  implicit class ExceptionWithStackTrace(val ex: Throwable) extends AnyVal {
    def stackTraceAsString: String = {
      val sw = new StringWriter()
      ex.printStackTrace(new PrintWriter(sw))
      sw.toString
    }
  }

}
