package com.samujjwaal.hw2

import com.samujjwaal.hw2.mappers.{CoAuthorCountMapper, VenueOneAuthorMapper, VenueTopPubMapper, VenueTopTenAuthorsMapper}
import com.samujjwaal.hw2.reducers._
import com.samujjwaal.hw2.util.XmlInputFormatWithMultipleTags
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{IntWritable, Text}
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.{FileOutputFormat, TextOutputFormat}
import org.slf4j.{Logger, LoggerFactory}


object RunJobs {

  def main(args: Array[String]): Unit = {
    val logger: Logger = LoggerFactory.getLogger(this.getClass)

    val conf: Config = ConfigFactory.load("JobSpecs.conf")

    val configuration = new Configuration

    //get start and end tags for each publication type
    configuration.set("xmlinput.start", conf.getString("start_xml_tags"))
    configuration.set("xmlinput.end", conf.getString("end_xml_tags"))

    configuration.set(TextOutputFormat.SEPARATOR, ",")

    configuration.set(
      "io.serializations",
      "org.apache.hadoop.io.serializer.JavaSerialization,org.apache.hadoop.io.serializer.WritableSerialization")

    if (args(0) == "1") {
      val venuePubOneAuthor = Job.getInstance(configuration, "Top 10 published authors at each venue")
      venuePubOneAuthor.setJarByClass(this.getClass)
      //Setting mapper
      venuePubOneAuthor.setMapperClass(classOf[VenueTopTenAuthorsMapper])
      venuePubOneAuthor.setInputFormatClass(classOf[XmlInputFormatWithMultipleTags])
      //setting reducer
      venuePubOneAuthor.setReducerClass(classOf[VenueTopTenAuthorsReducer])
      venuePubOneAuthor.setMapOutputKeyClass(classOf[Text])
      venuePubOneAuthor.setMapOutputValueClass(classOf[Text])
      venuePubOneAuthor.setOutputKeyClass(classOf[Text])
      venuePubOneAuthor.setOutputValueClass(classOf[Text])
      FileInputFormat.addInputPath(venuePubOneAuthor, new Path(args(1)))
      FileOutputFormat.setOutputPath(venuePubOneAuthor, new Path(conf.getString("master_output_path") + conf.getString("Job1_output_path")))
      logger.info("Starting up MapReduce job..")
      venuePubOneAuthor.waitForCompletion(true)
    }

    if (args(0) == "2") {
      val venuePubOneAuthor = Job.getInstance(configuration, "List of publications with only One author for each venue")
      venuePubOneAuthor.setJarByClass(this.getClass)
      //Setting mapper
      venuePubOneAuthor.setMapperClass(classOf[VenueOneAuthorMapper])
      venuePubOneAuthor.setInputFormatClass(classOf[XmlInputFormatWithMultipleTags])
      //setting reducer
      venuePubOneAuthor.setReducerClass(classOf[VenueOneAuthorReducer])
      venuePubOneAuthor.setMapOutputKeyClass(classOf[Text])
      venuePubOneAuthor.setMapOutputValueClass(classOf[Text])
      venuePubOneAuthor.setOutputKeyClass(classOf[Text])
      venuePubOneAuthor.setOutputValueClass(classOf[Text])
      FileInputFormat.addInputPath(venuePubOneAuthor, new Path(args(1)))
      FileOutputFormat.setOutputPath(venuePubOneAuthor, new Path(conf.getString("master_output_path") + conf.getString("Job2_output_path")))
      logger.info("Starting up MapReduce job..")
      venuePubOneAuthor.waitForCompletion(true)
    }

    if (args(0) == "3") {
      val venuePubOneAuthor = Job.getInstance(configuration, "Publication with highest number of authors at each venue")
      venuePubOneAuthor.setJarByClass(this.getClass)
      //Setting mapper
      venuePubOneAuthor.setMapperClass(classOf[VenueTopPubMapper])
      venuePubOneAuthor.setInputFormatClass(classOf[XmlInputFormatWithMultipleTags])
      //setting reducer
      venuePubOneAuthor.setReducerClass(classOf[VenueTopPubReducer])
      venuePubOneAuthor.setMapOutputKeyClass(classOf[Text])
      venuePubOneAuthor.setMapOutputValueClass(classOf[Text])
      venuePubOneAuthor.setOutputKeyClass(classOf[Text])
      venuePubOneAuthor.setOutputValueClass(classOf[Text])
      FileInputFormat.addInputPath(venuePubOneAuthor, new Path(args(1)))
      FileOutputFormat.setOutputPath(venuePubOneAuthor, new Path(conf.getString("master_output_path") + conf.getString("Job3_output_path")))
      logger.info("Starting up MapReduce job..")
      venuePubOneAuthor.waitForCompletion(true)
    }

    if (args(0) == "4") {
      val authorCount1 = Job.getInstance(configuration, "List of top 100 authors who publish with most co-authors(in desc. order)")
      authorCount1.setJarByClass(this.getClass)
      //Setting mapper
      authorCount1.setMapperClass(classOf[CoAuthorCountMapper])
      authorCount1.setInputFormatClass(classOf[XmlInputFormatWithMultipleTags])
      //setting reducer
      authorCount1.setReducerClass(classOf[MostCoAuthorCountReducer])
      authorCount1.setMapOutputKeyClass(classOf[Text])
      authorCount1.setMapOutputValueClass(classOf[IntWritable])
      authorCount1.setOutputKeyClass(classOf[Text])
      authorCount1.setOutputValueClass(classOf[Text])
      FileInputFormat.addInputPath(authorCount1, new Path(args(1)))
      FileOutputFormat.setOutputPath(authorCount1, new Path(conf.getString("master_output_path") + conf.getString("Job4_output_path")))
      logger.info("Starting up MapReduce job..")
      authorCount1.waitForCompletion(true)
    }

    if (args(0) == "5") {
      val authorCount2 = Job.getInstance(configuration, "List of 100 authors who publish without co-authors")
      authorCount2.setJarByClass(this.getClass)
      //Setting mapper
      authorCount2.setMapperClass(classOf[CoAuthorCountMapper])
      authorCount2.setInputFormatClass(classOf[XmlInputFormatWithMultipleTags])
      //setting reducer
      authorCount2.setReducerClass(classOf[ZeroCoAuthorCountReducer])
      authorCount2.setMapOutputKeyClass(classOf[Text])
      authorCount2.setMapOutputValueClass(classOf[IntWritable])
      authorCount2.setOutputKeyClass(classOf[Text])
      authorCount2.setOutputValueClass(classOf[Text])
      FileInputFormat.addInputPath(authorCount2, new Path(args(1)))
      FileOutputFormat.setOutputPath(authorCount2, new Path(conf.getString("master_output_path") + conf.getString("Job5_output_path")))
      logger.info("Starting up MapReduce job..")
      authorCount2.waitForCompletion(true)
    }
  }
}
