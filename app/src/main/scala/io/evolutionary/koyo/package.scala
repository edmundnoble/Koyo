package io.evolutionary

import java.util
import java.util.concurrent._

import android.os.{AsyncTask, Looper, Handler}
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.{EditText, TextView}
import com.squareup.okhttp.Response

import scala.concurrent.ExecutionContext
import scala.language.implicitConversions
import scalaz.concurrent.{Task, Strategy}
import scalaz._
import Scalaz._

package object koyo {
  type OkCallback = (Throwable \/ Response) => Unit

  implicit def toRunnable[T](block: => T): Runnable = new Runnable {
    override def run(): Unit = block
  }

  implicit def toOnClickListener[T](block: (View) => T): OnClickListener = new OnClickListener {
    override def onClick(view: View): Unit = block(view)
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

  def onMainThread: Boolean =
    Looper.myLooper == Looper.getMainLooper

  implicit class TaskOnMainThread[T](val t: Task[T]) extends AnyVal {
    def ui: Task[Unit] = Task.delay(runOnMainThread {
      t.run
    })
  }

  implicit class TaskOnBackgroundThread[T](val t: Task[T]) extends AnyVal {
    def bg: Task[T] = Task.async[T] { cb =>
      if (!onMainThread) {
        cb(t.attemptRun)
      }
      else {
        val task = new AsyncTask[AnyRef, Void, AnyRef] {
          // This looks weird, doesn't it? Thanks to SI-1459 (and maybe another bug),
          // everything here has to be AnyRef.
          override def doInBackground(paramses: AnyRef*): AnyRef = { Log.d("Package", "Doing something in the background..."); t.attemptRun }

          override def onPostExecute(result: AnyRef): Unit = { Log.d("Package", "Finished doing something in the background..."); cb(result.asInstanceOf[Throwable \/ T]) }

          override def onCancelled(): Unit = cb(new RuntimeException().left[T])
        }.execute()
      }
    }
  }

}
