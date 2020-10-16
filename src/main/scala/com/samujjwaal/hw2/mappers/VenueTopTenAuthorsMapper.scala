package com.samujjwaal.hw2.mappers

import com.samujjwaal.hw2.util.MapReduceUtils._
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.Mapper
import org.slf4j.{Logger, LoggerFactory}

/**
 * Mapper class to populate list of top 10 authors published at each venue
 */
class VenueTopTenAuthorsMapper extends Mapper[LongWritable, Text, Text, Text] {

  val logger: Logger = LoggerFactory.getLogger(this.getClass)

  override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, Text]#Context): Unit = {
    // parse xml record
    val parsedXML = parseXML(value.toString)
    // check if author tag exists for record
    if (getAuthorType(parsedXML) != null) {
      // get venue type for publication
      val venueType = getPubVenueType(parsedXML)
      // get list of authors for publication
      val authorList = getXMLTagContent(parsedXML, getAuthorType(parsedXML))

      if (authorList.nonEmpty) {
        //        logger.info("Number of Authors: " + authorList)

        authorList.foreach(author => {
          venueType match {
            case "www" => context.write(new Text(venueType), new Text((author, 1).toString()))
            case _ =>
              // get venue name for publication
              val pubVenue = removePunctuations(getXMLTagContent(parsedXML, venueType).head)
              logger.info("Venue Name: {}, Author Name: {}", pubVenue, author)
              context.write(new Text(pubVenue), new Text((author, 1).toString()))
          }
          // mapper outputs key:<publication venue> & value:<(author name, 1))>
        })
      }
    }
  }
}

