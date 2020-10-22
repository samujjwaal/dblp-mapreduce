package com.samujjwaal.hw2.reducers

import java.lang

import com.samujjwaal.hw2.util.MapReduceUtils.removePunctuations
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Reducer
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.mutable
import scala.jdk.CollectionConverters.IterableHasAsScala

/**
 * Reducer class to find the publication with max. no. of authors at each venue
 */
class VenueTopPubReducer extends Reducer[Text, Text, Text, Text] {

  val logger: Logger = LoggerFactory.getLogger(this.getClass)

  override def reduce(key: Text, values: lang.Iterable[Text], context: Reducer[Text, Text, Text, Text]#Context): Unit = {
    val map: mutable.Map[String, Integer] = mutable.LinkedHashMap[String, Integer]()

    // add publication name and its author count to hashmap for a venue(key)
    values.asScala.foreach(pub => {
      val pubName = removePunctuations(pub.toString.split(",").head)
      val authorCount = removePunctuations(pub.toString.split(",")(1))
      map.put(pubName, authorCount.toInt)
    })

    // sort hashmap and get publication name with max author count
    val topPub = mutable.LinkedHashMap(map.toSeq.sortWith(_._2 > _._2): _*).head._1

    logger.info("Venue: {}, Publication: {}", key.toString, topPub)
    context.write(new Text(key), new Text(topPub))

    // reducer outputs key:<venue name> & value:<publication name>
  }
}
