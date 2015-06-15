package io.evolutionary

import java.util
import java.util.concurrent._

import android.os.{AsyncTask, Looper, Handler}
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

  def runOnMainThread(block: Runnable): Unit = {
    if (!onMainThread)
      new Handler(Looper.getMainLooper) post block
    else
      block.run()
  }

  def onMainThread: Boolean =
    Looper.myLooper == Looper.getMainLooper

  implicit class TaskOnMainThread[T](t: Task[T]) {
    def ui: Task[Unit] = Task.delay(runOnMainThread {
      t.run
    })
  }

  implicit class TaskOnBackgroundThread[T](t: Task[T]) {
    def bg: Task[T] = Task.async[T] { cb =>
      val task = new AsyncTask[Void, Void, Throwable \/ T] {
        override def doInBackground(paramses: Void*): Throwable \/ T = t.attemptRun

        override def onPostExecute(result: Throwable \/ T): Unit = cb(result)

        override def onCancelled(): Unit = cb(new RuntimeException().left[T])
      }.execute()
    }
  }

}
