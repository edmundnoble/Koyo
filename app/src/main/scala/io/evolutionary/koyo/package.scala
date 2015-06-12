package io.evolutionary

import java.util
import java.util.concurrent._

import android.os.{AsyncTask, Looper, Handler}

import scala.concurrent.ExecutionContext
import scala.language.implicitConversions
import scalaz.concurrent.{Task, Strategy}
import scalaz._
import Scalaz._

package object koyo {
  type Delegated[+A] = Kleisli[Task, ExecutorService, A]
  implicit def toRunnable[T](block: => T): Runnable = new Runnable {
    override def run(): Unit = block
  }
  def runOnMainThread(block: Runnable): Unit = {
    new Handler(Looper.getMainLooper) post block
  }
  implicit class TaskOnMainThread[T](t: Task[T]) {
    def ui = Task.delay(runOnMainThread {
      t.run
    })
  }
  val mainThreadCtxt = new ExecutionContext {
    override def reportFailure(cause: Throwable): Unit = ???
    override def execute(runnable: Runnable): Unit = runOnMainThread(runnable)
  }
  val mainThreadStrategy = new Strategy {
    override def apply[A](a: => A) = () => runOnMainThread(a).asInstanceOf[A]
  }
  val backgroundThreadStrategy = new Strategy {
    override def apply[A](a: => A): () => A = {
      val task = new AsyncTask[Void, Void, A] {
        override def doInBackground(paramses: Void*): A = a
      }.execute()
      () => task.get()
    }
  }
}
