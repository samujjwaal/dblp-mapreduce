package com.samujjwaal.hw2.mappers

import com.samujjwaal.hw2.util.MapReduceUtils._
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.Mapper
import org.slf4j.{Logger, LoggerFactory}

class VenueTopPubMapper extends Mapper[LongWritable, Text, Text, Text] {

  val logger: Logger = LoggerFactory.getLogger(this.getClass)

  override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, Text]#Context): Unit = {
    // parse xml record
    val parsedXML = parseXML(value.toString)
    // check if author tag exists for record
    if (getAuthorType(parsedXML) != null) {
      // get number of authors for publication
      val numberAuthors = getXMLTagContent(parsedXML, getAuthorType(parsedXML)).length
      // get venue type for publication
      val venueType = getPubVenueType(parsedXML)
      // get publication name
      val pubName = removePunctuations((parsedXML \\ "title").head.text)

      venueType match {
        case "www" => context.write(new Text(venueType), new Text((pubName, numberAuthors).toString()))
        case _ =>
          // get venue name for publication
          val pubVenue = removePunctuations(getXMLTagContent(parsedXML, venueType).head)
          logger.info("Venue Name: {}, Publication: {}, Number of Authors: {}", pubVenue, pubName, numberAuthors)
          context.write(new Text(pubVenue), new Text((pubName, numberAuthors).toString()))
      }
      // mapper outputs key:<publication venue> & value:<(publication name, number of authors)>
    }
  }
}

