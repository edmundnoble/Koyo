package io.evolutionary.koyo

import java.net.{CookieManager, CookiePolicy, URL}
import java.security.GeneralSecurityException
import javax.net.ssl._
import java.security.cert.X509Certificate

import android.util.Log
import com.squareup.mimecraft.FormEncoding
import com.squareup.okhttp._
import io.evolutionary.koyo.parsing.{TablePage, HtmlParser}

import scala.concurrent.{Promise, Future}
import scalaz.concurrent.{Strategy, Task}
import java.io.IOException
import scalaz._
import Scalaz._

object Jobmine {

  object Links {
    val Documents = new URL("https://jobmine.ccol.uwaterloo.ca/psc/SS/EMPLOYEE/WORK/c/UW_CO_STUDENTS.UW_CO_STUDDOCS")
    val Profile = new URL("https://jobmine.ccol.uwaterloo.ca/psc/SS/EMPLOYEE/WORK/c/UW_CO_STUDENTS.UW_CO_STUDENT")
    val Skills = new URL("https://jobmine.ccol.uwaterloo.ca/psc/SS/EMPLOYEE/WORK/c/UW_CO_STUDENTS.UW_CO_STUDENT?PAGE=UW_CO_STU_SKL_MTN")
    val JobSearch = new URL("https://jobmine.ccol.uwaterloo.ca/psc/SS/EMPLOYEE/WORK/c/UW_CO_STUDENTS.UW_CO_JOBSRCH")
    val Shortlist = new URL("https://jobmine.ccol.uwaterloo.ca/psc/SS/EMPLOYEE/WORK/c/UW_CO_STUDENTS.UW_CO_JOB_SLIST")
    val Applications = new URL("https://jobmine.ccol.uwaterloo.ca/psc/SS/EMPLOYEE/WORK/c/UW_CO_STUDENTS.UW_CO_APP_SUMMARY")
    val Interviews = new URL("https://jobmine.ccol.uwaterloo.ca/psc/SS/EMPLOYEE/WORK/c/UW_CO_STUDENTS.UW_CO_STU_INTVS")
    val Rankings = new URL("https://jobmine.ccol.uwaterloo.ca/psc/SS/EMPLOYEE/WORK/c/UW_CO_STUDENTS.UW_CO_RNK2")
    val Descriptions = new URL("https://jobmine.ccol.uwaterloo.ca/psc/SS/EMPLOYEE/WORK/c/UW_CO_STUDENTS.UW_CO_JOBDTLS?UW_CO_JOB_ID=")
    val Login = new URL("https://jobmine.ccol.uwaterloo.ca/psp/SS/?cmd=login&languageCd=ENG&sessionId=")
    val Logout = new URL("https://jobmine.ccol.uwaterloo.ca/psp/SS/?cmd=login&languageCd=ENG&")
  }

  def buildTablePageViews(page: TablePage)(implicit client: OkHttpClient): Task[Option[Seq[page.RowModel]]] = {
    val request = new Request.Builder()
      .url(page.url)
      .get()
      .build()
    asyncRequest(request) map { response =>
      val html = response.body().html
      page.tableNames.foldLeft(Option(Seq.empty[page.RowModel])) {
        case (acc, (tableType, tableName)) =>
          val rowsMaybe = HtmlParser.makeRowsFromHtml(tableName, html)
          val rowsModeled = rowsMaybe.map(rows => page.tableToViews(rows.map(row => (tableType, row))))
          for {
            initial <- acc
            newRows <- rowsModeled
          } yield initial ++ newRows
      }
    }
  }

  def asyncRequest(request: Request)(implicit client: OkHttpClient): Task[Response] = {
    Log.d("Jobmine", "Making an asyncRequest...")
    Task.async { cb: OkCallback =>
      val call = client.newCall(request)
      Log.d("Jobmine", "asyncRequest task running...")
      call.enqueue(new Callback() {
        override def onFailure(request: Request, e: IOException) = cb(-\/(e))

        override def onResponse(response: Response) = cb(\/-(response))
      })
    }
  }

  def makeUnsafeClient(): OkHttpClient = {
    val okHttpClient = new OkHttpClient()
    try {
      val trustAllCerts = Array[TrustManager](
        new X509TrustManager() {
          override def checkClientTrusted(chain: Array[X509Certificate], authType: String) = ()

          override def checkServerTrusted(chain: Array[X509Certificate], authType: String) = ()

          override def getAcceptedIssuers = null
        }
      )

      // Install the all-trusting trust manager
      val sslContext = SSLContext.getInstance("SSL")
      sslContext.init(null, trustAllCerts, new java.security.SecureRandom())
      // Create an ssl socket factory with our all-trusting manager
      val sslSocketFactory = sslContext.getSocketFactory
      okHttpClient.setSslSocketFactory(sslSocketFactory)
      okHttpClient.setHostnameVerifier(new HostnameVerifier() {
        override def verify(hostname: String, session: SSLSession) = true
      })
    } catch {
      case _: GeneralSecurityException =>
    }
    // Jobmine needs cookies
    val cookieManager = new CookieManager()
    cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
    okHttpClient.setCookieHandler(cookieManager)
    okHttpClient
  }

}
