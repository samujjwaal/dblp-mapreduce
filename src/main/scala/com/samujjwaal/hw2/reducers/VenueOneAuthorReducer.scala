package com.samujjwaal.hw2.reducers

import java.lang

import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Reducer
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.mutable
import scala.jdk.CollectionConverters.IterableHasAsScala

/**
 * Reducer class to populate list of publications with only 1 author for each venue
 */
class VenueOneAuthorReducer extends Reducer[Text, Text, Text, Text] {

  val logger: Logger = LoggerFactory.getLogger(this.getClass)

  override def reduce(key: Text, values: lang.Iterable[Text], context: Reducer[Text, Text, Text, Text]#Context): Unit = {
    var pubList = mutable.ArrayBuffer[String]()

    // populate list of all publications for a venue(key)
    values.asScala.foreach(pub => {
      pubList += pub.toString
    })

    logger.info("Venue: {}, Publication list: {}", key.toString, pubList.toList)
    context.write(new Text(key), new Text(pubList.toList.toString().replaceAll(",", ";")))

    // reducer outputs key:<venue name> & value:<list of publications>
  }
}
