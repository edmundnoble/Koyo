package io.evolutionary.koyo.parsing

import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import org.jsoup.select.Elements
import scala.collection.JavaConverters._

object HtmlParser {
  val tableCSSClass = "PSLEVEL1GRIDWBO"



  @inline def getTag(elem: Element, tag: String): Seq[Element] = elem.getElementsByTag(tag).asScala

  def makeRowsFromHtml(tableName: String, htmlDocument: Document): Option[Seq[Map[String, String]]] = {

    val parseResult: Option[(Seq[String], Seq[Seq[String]])] = // So the structure of the jobmine tables goes thus:
      for {
        origTable <- htmlDocument.getElementsByClass(tableCSSClass).iterator().asScala.find(_.id() == tableName) // There are TWO tables with the same ID. So filter them out by class first.
        rowElements = for {
          tbody <- getTag(origTable, "tbody") // Table body
          tr <- getTag(tbody, "tr")
          td <- getTag(tr, "td")
          realTable <- getTag(td, "table") // Yo dawg, I heard you like tables
          realTBody <- getTag(realTable, "tbody")
          rowElements <- getTag(realTBody, "tr") // The row HTML elements.
        } yield rowElements
        headerRowElem <- rowElements.headOption
        realRowElems = rowElements.tail // The first is the headers, the rest is the actual rows.
        columnHeaders = getTag(headerRowElem, "th").map(_.text)
        rows = realRowElems.map(getTag(_, "td").map(_.text))
      } yield (columnHeaders, rows)
    parseResult map {
      case (headers, rows) =>
        rows.map(row => headers.zip(row).toMap)
    }
  }
}
