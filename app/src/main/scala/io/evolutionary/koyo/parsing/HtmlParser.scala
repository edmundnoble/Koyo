package io.evolutionary.koyo.parsing

import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._
import net.ruippeixotog.scalascraper.scraper.{ContentExtractors, SimpleExtractor}
import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}

object HtmlParser {
  val tableCSSClass = "PSLEVEL1GRIDWBO"

  def makeRowsFromHtml(tableName: String, htmlDocument: Document): Option[Seq[Map[String, String]]] = {

    val parseResult = // So the structure of the jobmine tables goes thus:
      for {
        origTable <- htmlDocument.getElementsByClass(tableCSSClass).find(_.id() == tableName) // There are TWO tables with the same ID. So filter them out by class first.
        tbody <- origTable >?> extractor("tbody", element) // Table body
        tr <- tbody >?> extractor("tr", elements) // Et cetera.
        td <- tr >?> extractor("td", element)
        realTable <- td >?> extractor("table", element) // Yo dawg, I heard you like tables
        realTBody <- realTable >?> extractor("tbody", element)
        rowElements <- realTBody >?> extractor("tr", elements) // The row HTML elements.
        (headerRowElem +: realRowElems) = rowElements.toVector // The first is the headers, the rest is the actual rows.
        columnHeaders <- headerRowElem >?> extractor("th", texts)
        rows = realRowElems.map(_ >> extractor("td", texts))
      } yield (columnHeaders, rows)
    parseResult map {
      case (headers, rows) =>
        rows.map(row => headers.zip(row).toMap)
    }
  }
}
