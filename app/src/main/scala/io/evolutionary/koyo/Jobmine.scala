package io.evolutionary.koyo

import java.net.{CookieManager, CookiePolicy, URL}
import java.security.GeneralSecurityException
import java.util.concurrent.TimeUnit
import javax.net.ssl._
import java.security.cert.X509Certificate

import android.content.Context
import android.util.Log
import com.squareup.mimecraft.FormEncoding
import com.squareup.okhttp._
import io.evolutionary.koyo.parsing.{TablePage, HtmlParser}
import org.jsoup.nodes.Document

import scala.concurrent.{Promise, Future}
import scala.collection.mutable
import scalaz.{\/-, -\/}
import scalaz.concurrent.{Strategy, Task}
import java.io.IOException

object Jobmine {

  implicit val tag = LogTag("Jobmine")

  object Links {
    val Documents = new URL("https://jobmine.ccol.uwaterloo.ca/psc/SS/EMPLOYEE/WORK/c/UW_CO_STUDENTS.UW_CO_STUDDOCS")
    val Profile = new URL("https://jobmine.ccol.uwaterloo.ca/psc/SS/EMPLOYEE/WORK/c/UW_CO_STUDENTS.UW_CO_STUDENT")
    val Skills = new URL("https://jobmine.ccol.uwaterloo.ca/psc/SS/EMPLOYEE/WORK/c/UW_CO_STUDENTS.UW_CO_STUDENT?PAGE=UW_CO_STU_SKL_MTN")
    val JobSearch = new URL("https://jobmine.ccol.uwaterloo.ca/psc/SS/EMPLOYEE/WORK/c/UW_CO_STUDENTS.UW_CO_JOBSRCH")
    //val JobSearch = new URL("https://jobmine.ccol.uwaterloo.ca/psc/SS/EMPLOYEE/WORK/c/UW_CO_STUDENTS.UW_CO_JOBSRCH.GBL?pslnkid=UW_CO_JOBSRCH_LINK&FolderPath=PORTAL_ROOT_OBJECT.UW_CO_JOBSRCH_LINK&IsFolder=false&IgnoreParamTempl=FolderPath%2cIsFolder&PortalActualURL=https%3a%2f%2fjobmine.ccol.uwaterloo.ca%2fpsc%2fSS%2fEMPLOYEE%2fWORK%2fc%2fUW_CO_STUDENTS.UW_CO_JOBSRCH.GBL%3fpslnkid%3dUW_CO_JOBSRCH_LINK&PortalContentURL=https%3a%2f%2fjobmine.ccol.uwaterloo.ca%2fpsc%2fSS%2fEMPLOYEE%2fWORK%2fc%2fUW_CO_STUDENTS.UW_CO_JOBSRCH.GBL%3fpslnkid%3dUW_CO_JOBSRCH_LINK&PortalContentProvider=WORK&PortalCRefLabel=Job%20Inquiry&PortalRegistryName=EMPLOYEE&PortalServletURI=https%3a%2f%2fjobmine.ccol.uwaterloo.ca%2fpsp%2fSS%2f&PortalURI=https%3a%2f%2fjobmine.ccol.uwaterloo.ca%2fpsc%2fSS%2f&PortalHostNode=WORK&NoCrumbs=yes&PortalKeyStruct=yes")
    val Shortlist = new URL("https://jobmine.ccol.uwaterloo.ca/psc/SS/EMPLOYEE/WORK/c/UW_CO_STUDENTS.UW_CO_JOB_SLIST")
    val Applications = new URL("https://jobmine.ccol.uwaterloo.ca/psc/SS/EMPLOYEE/WORK/c/UW_CO_STUDENTS.UW_CO_APP_SUMMARY")
    val Interviews = new URL("https://jobmine.ccol.uwaterloo.ca/psc/SS/EMPLOYEE/WORK/c/UW_CO_STUDENTS.UW_CO_STU_INTVS")
    val Rankings = new URL("https://jobmine.ccol.uwaterloo.ca/psc/SS/EMPLOYEE/WORK/c/UW_CO_STUDENTS.UW_CO_RNK2")
    val Descriptions = new URL("https://jobmine.ccol.uwaterloo.ca/psc/SS/EMPLOYEE/WORK/c/UW_CO_STUDENTS.UW_CO_JOBDTLS?UW_CO_JOB_ID=")
    val Login = new URL("https://jobmine.ccol.uwaterloo.ca/psp/SS/?cmd=login&languageCd=ENG&sessionId=")
    val Logout = new URL("https://jobmine.ccol.uwaterloo.ca/psp/SS/?cmd=login&languageCd=ENG&")
  }

  def requestTablePageRows[T, P](page: TablePage[T, P])(implicit client: OkHttpClient): Task[Seq[T]] = {
    val request = new Request.Builder()
      .url(page.url)
      .build()
    asyncRequest(request) map { response => parseTablePageRows(page, response.body().html) }
  }

  def parseTablePageRows[R, T](page: TablePage[R, T], html: Document): Seq[R] = {
    val tables = mutable.Map[T, Seq[page.RawRow]]()
    page.tableNames.foldLeft(()) {
      (acc, tableInfo) =>
        val (tableType, tableName) = tableInfo
        val rowsMaybe = HtmlParser.makeRowsFromHtml(tableName, html)
        tables += (tableType -> (rowsMaybe getOrElse Seq.empty))
    }
    page.tablesToRows(tables.toMap)
  }

  def asyncRequest(request: Request)(implicit client: OkHttpClient): Task[Response] = {
    debug("Making an asyncRequest...")
    Task.async { cb: OkCallback =>
      val call = client.newCall(request)
      debug("asyncRequest task running...")
      call.enqueue(new Callback() {
        override def onFailure(request: Request, e: IOException) = cb(-\/(e))

        override def onResponse(response: Response) = cb(\/-(response))
      })
    }
  }

  def makeUnsafeClient()(implicit context: Context): OkHttpClient = {
    val okHttpClient = new OkHttpClient()
    okHttpClient.setConnectTimeout(5L, TimeUnit.SECONDS)
    okHttpClient.setReadTimeout(5L, TimeUnit.SECONDS)
    okHttpClient.setWriteTimeout(5L, TimeUnit.SECONDS)
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
    //val cookieManager = new CookieManager(new PersistentCookieStore(context.getApplicationContext), CookiePolicy.ACCEPT_ALL)
    //okHttpClient.setCookieHandler(cookieManager)
    okHttpClient
  }

}
