package io.evolutionary.koyo.parsing

import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._
import net.ruippeixotog.scalascraper.scraper.{ContentExtractors, SimpleExtractor}
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

object HtmlParser {
  val tableCSSClass = "PSLEVEL1GRIDWBO"

  def makeRowsFromHtml(tableName: String, html: String): Option[Seq[Map[String, String]]] = {
    val htmlParsed = Jsoup.parse(html)
    val parseResult = // So the structure of the jobmine tables goes thus:
      for {
        origTable <- htmlParsed.getElementsByClass(tableCSSClass).find(_.id() == tableName) // There are TWO tables with the same ID. So filter them out by class first.
        tbody <- origTable >?> extractor("tbody", element) // Table body
        tr <- tbody >?> extractor("tr", elements)
        td <- tr >?> extractor("td", element) // Got to get the last tr, because the first is yet another header
        realTable <- td >?> extractor("table", element) // New table
        realTBody <- realTable >?> extractor("tbody", element) // New body
        rowElements <- realTBody >?> extractor("tr", elements) // Rows
        columnHeaders <- rowElements.head >?> extractor("th", elements) // Column headers are always the first row in the table.
        rows = rowElements.tail.map(_ >> extractor("td", texts)) // Actual rows.
      } yield (columnHeaders, rows)
    parseResult map {
      case (headers, rows) =>
        rows.map(row => headers.map(_.text).zip(row).toMap)
    }
  }
}
