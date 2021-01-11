package com.samujjwaal.hw2.reducers

import java.lang
import org.apache.hadoop.io.{IntWritable, Text}
import org.apache.hadoop.mapreduce.Reducer
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.mutable
import scala.jdk.CollectionConverters.IterableHasAsScala

/**
 * Reducer class to calculate the number of co-authors for each author
 */
class CoAuthorCountReducer extends Reducer[Text, IntWritable, Text, Text] {

  val logger: Logger = LoggerFactory.getLogger(this.getClass)
  // hashmap to store author name and max coauthor count to sort at end of reduce task
  var map: mutable.Map[String, Integer] = mutable.LinkedHashMap[String, Integer]()

  override def reduce(key: Text, values: lang.Iterable[IntWritable], context: Reducer[Text, IntWritable, Text, Text]#Context): Unit = {
    var list = mutable.ArrayBuffer[Int]()

    // populate list of co-author count of an author(key) for all his publications
    values.asScala.foreach(record => {
      list += record.get()
    })
    // add max coauthor count to hashmap
    map.put(key.toString, list.max)
    logger.info("Maximum no of co-authors for author {}: {}", key.toString, list.max)
  }

  /**
   * This method is called at the end of all reduce tasks of the job to sort all authors
   */
  override def cleanup(context: Reducer[Text, IntWritable, Text, Text]#Context): Unit = {

    val outputFlag = context.getConfiguration.get("jobNo")

    // find top 100 authors who have published with the maximum number of co-authors
    if (outputFlag == "4"){

      // sort hashmap in descending order by coauthor count of each author and select top 100
      val sortedMap = mutable.LinkedHashMap(map.toSeq.sortWith(_._2 > _._2): _*).take(100)

      logger.info("Authors: {}", sortedMap.keys)
      sortedMap.foreach(record => {
        context.write(new Text(record._1), new Text(record._2.toString))
      })

      // reducer outputs key:<author name> & value:<max. number of coauthors>
    }

    // find 100 authors who have published with 0 co-authors
    if (outputFlag == "5"){

      // filter hashmap by coauthor count equals 0 and select 100
      val outputMap = map.filter(_._2 == 0).take(100)

      logger.info("Authors: {}", outputMap.keys)
      outputMap.foreach(record => {
        context.write(new Text(record._1), new Text(record._2.toString))
      })

      // reducer outputs key:<author name> & value:<0>
    }

  }
}
