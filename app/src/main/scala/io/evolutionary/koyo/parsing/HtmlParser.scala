package io.evolutionary.koyo.parsing

import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

object HtmlParser {
  def makeRowsFromHtml(tableName: String, html: String): Option[Map[String, String]] = {
    val htmlParsed = Jsoup.parse(html)
    val rowElems = {
      val tr = htmlParsed >?> elements("tbody")
    }
    /*
    val rowElems = for {
      body <- htmlParsed >> element("tbody")
      tr <- body >> element("tr")
      td <- tr >> "td"
      table <- td >> "table"
      tbody <- table >> "tbody"
    } yield tbody
    boolean verifySingleOutline = outlines.length == 1;
    SimpleHtmlParser parser = new SimpleHtmlParser(html);
    String text, tableID = outlines[ 0].getTableId();
    val index = html.indexOf(tableName)
    val start = html.indexOf("<th", index)
    val end = html.indexOf("<tr", start)


    TableParserOutline passOutline = null;
    // See if each header matches
    HEADER[] headers = outlines[ 0].getHeaders();
    for (i = 0;
    i < headers.length;
    i ++)
    {
      text = parser.getTextInNextElement("th").toLowerCase(Locale.getDefault()).replace("  ", " ");
      if (parser.getPosition() < end) {
        if (!text.equals(headers[i].toString())) {
          throw new HiddenColumnsException("Outline does not match html");
        }
      } else {
        throw new HiddenColumnsException("Outline has more columns than html");
      }
    }
    passOutline = outlines[ 0];

    // Get the html for the table itself
    end = parser.skipText("</table>");
    html = html.substring(start, end);

    internalExecute(passOutline, html);
  }

  /**
   * This parses the table based on the table HTML. This requires the HTML to be
   * <tr>....</tr>. The execute method in this class calls it correctly. Any errors
   * in parsing will throw JbmnplsParsingException. This is much faster than using
   * a 3rd party HTML parser because this is 4 times faster. When complete it will
   * return mid-code.
   * @param html
   */
  private void parseTable (TableParserOutline outline, String html) {
    SimpleHtmlParser parser = new SimpleHtmlParser(html);
    HEADER[] headers = outline.getHeaders();

    int row = 0;
    Object[] passedObj = new Object[outline.columnLength()];
    while (!parser.isEndOfContent() && row < INFINITE_LOOP_LIMIT) {
      // Check if there is another TD, if not we are done
      int position = html.indexOf("<tr", parser.getPosition());
      if (position == -1) {
        return;
      }
      parser.setPosition(position);

      // Parse the job id of the table, if no id, then table is empty
      String text = parser.getTextInNextTD();
      if (text == "") {
        return;
      }
      try {
        passedObj[ 0] = Integer.parseInt(text);
      } catch (NumberFormatException e) {
        e.printStackTrace();
        throw new HiddenColumnsException("Cannot get id from table.");
      }

      for (int i = 1;
      i < outline.columnLength();
      i ++)
      {
        text = parser.getTextInNextTD();

        // Convert the value to the column type and type
        switch(headers[i]) {
          // Strings
          case JOB_TITLE:
          case EMPLOYER:
          case EMPLOYER_NAME:
          case UNIT:
          case UNIT_NAME:
          case UNIT_NAME_1:
          case TERM:
          case ROOM:
          case INSTRUCTIONS:
          case INTERVIEWER:
          case LOCATION:
          case START_TIME: // Handled by interviews
          case END_TIME: // Handled by interviews
          case SHORTLIST:
          value = text;
          break;

          // Integers
          case OPENINGS:
          case NUM_APPS:
          case LENGTH:
            value = text == "" ? 0: Integer.parseInt (text);
          break;

          case JOB_ID:
          case JOB_IDENTIFIER:
          if (text == "") {
            // No data in table/row
            if (row != 0) {
              throw new JbmnplsParsingException("Cannot parse id because it is empty on row= " + row);
            }
            return;
          }
          value = text == "" ? 0: Integer.parseInt (text);
          break;

          // Dates
          case LAST_DAY_TO_APPLY:
          case LAST_DATE_TO_APPLY:
          case DATE:
          if (text.equals("")) {
            value = new Date(0);
          } else {
            try {
              if (text.contains("-")) {
                value = DATE_FORMAT_DASH.parse(text);
              } else {
                value = DATE_FORMAT_SPACE.parse(text);
              }
            } catch (ParseException e) {
              e.printStackTrace();
              value = new Date(0);
            }
          }
          break;

          // Interview Type
          case INTER_TYPE:
            value = INTERVIEW_TYPE.getTypefromString(text);
          break;

          // Application Status
          case APPLY:
            value = APPLY_STATUS.getApplicationStatusfromString(text);
          break;

          // Job Status
          case APP_STATUS:
            value = STATUS.getStatusfromString(text);
          break;

          // Job State
          case JOB_STATUS:
            value = STATE.getStatefromString(text);
          break;

          // Ignore
          case VIEW_DETAILS:
          case VIEW_PACKAGE:
          case SELECT_TIME:
          case BLANK:
            break;

        }
        passedObj[i] = value;
      }
      // Now we pass the values back to the activities to make jobs
      listener.onRowParse(outline, passedObj);
      row ++;
    }
  }
*/
    None
  }
}