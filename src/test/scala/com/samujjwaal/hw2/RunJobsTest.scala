package com.samujjwaal.hw2

import com.typesafe.config.{Config, ConfigFactory}
import org.scalatest.funsuite.AnyFunSuite

class RunJobsTest extends AnyFunSuite {

  var jobConfig: Config = _

  test("Check if config files exists") {
    jobConfig = ConfigFactory.load("JobSpecs")
    assert(!jobConfig.isEmpty)
  }
  test("Check opening and closing xml tags are the same") {
    jobConfig.getString("start_xml_tags")
    jobConfig.getString("end_xml_tags")
    assert(jobConfig.getString("start_xml_tags").split(",").length == jobConfig.getString("end_xml_tags").split(",").length)
  }

}
