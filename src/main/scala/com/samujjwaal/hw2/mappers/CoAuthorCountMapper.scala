package com.samujjwaal.hw2.mappers

import com.samujjwaal.hw2.util.MapReduceUtils.{getAuthorType, getXMLTagContent, parseXML, removePunctuations}
import org.apache.hadoop.io.{IntWritable, LongWritable, Text}
import org.apache.hadoop.mapreduce.Mapper
import org.slf4j.{Logger, LoggerFactory}


class CoAuthorCountMapper extends Mapper[LongWritable, Text, Text, IntWritable] {

  val logger: Logger = LoggerFactory.getLogger(this.getClass)

  override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, IntWritable]#Context): Unit = {
    // parse xml record
    val parsedXML = parseXML(value.toString)
    // check if author tag exists for record
    if (getAuthorType(parsedXML) != null) {
      // get list of authors for publication
      val authorList = getXMLTagContent(parsedXML, getAuthorType(parsedXML))
      logger.info("Authors: {}", authorList.toString())
      if (authorList.nonEmpty) {
        authorList.foreach(author => {
          logger.info("Author: {}, CoAuthors Count: {}", author, authorList.length - 1)
          context.write(new Text(removePunctuations(author)), new IntWritable(authorList.length - 1))
        })
        // mapper outputs key:<publication author name> & value:<number of co-authors>
      }
    }
  }
}
