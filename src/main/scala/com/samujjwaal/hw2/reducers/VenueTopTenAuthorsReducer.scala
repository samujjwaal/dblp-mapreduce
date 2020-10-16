package com.samujjwaal.hw2.reducers

import java.lang

import com.samujjwaal.hw2.util.MapReduceUtils.removePunctuations
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Reducer
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.mutable
import scala.jdk.CollectionConverters.IterableHasAsScala

/**
 * Reducer class to populate list of top 10 authors published at each venue
 */
class VenueTopTenAuthorsReducer extends Reducer[Text, Text, Text, Text] {

  val logger: Logger = LoggerFactory.getLogger(this.getClass)

  override def reduce(key: Text, values: lang.Iterable[Text], context: Reducer[Text, Text, Text, Text]#Context): Unit = {
    val map: mutable.Map[String, Integer] = mutable.LinkedHashMap[String, Integer]()

    // add author name and no of times author appears in venue(key) to hashmap
    values.asScala.foreach(author => {
      val authorName = removePunctuations(author.toString.split(",").head)
      val authorCount = removePunctuations(author.toString.split(",")(1)).toInt  // always equal to 1

      // add or update hashmap by checking if key already exists
      map.updateWith(authorName) {
        case Some(v) => Some(v + authorCount)
        case None => Some(authorCount)
      }
    })

    // sort hashmap in descending order by author count and select top 10
    val topTenAuthors = mutable.LinkedHashMap(map.toSeq.sortWith(_._2 > _._2): _*).take(10).keys.toList

    logger.info("Venue: {}, Authors: {}",key.toString,topTenAuthors)
    context.write(new Text(key), new Text(topTenAuthors.toString().replaceAll(",",";")))

    // reducer outputs key:<venue name> & value:<list of authors>
  }
}
