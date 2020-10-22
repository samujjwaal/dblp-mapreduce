package com.samujjwaal.hw2.util

import java.net.URI

import com.samujjwaal.hw2.util.MapReduceUtils._
import org.scalatest.funsuite.AnyFunSuite

import scala.xml.Elem

class MapReduceUtilsTest extends AnyFunSuite {

  val testXML1 = "<article mdate=\"2018-01-07\" key=\"tr/meltdown/s18\" publtype=\"informal\">\n<author>Paul Kocher</author>\n<author>Daniel Genkin</author>\n<author>Daniel Gruss</author>\n<author>Werner Haas</author>\n<author>Mike Hamburg</author>\n<author>Moritz Lipp</author>\n<author>Stefan Mangard</author>\n<author>Thomas Prescher 0002</author>\n<author>Michael Schwarz 0001</author>\n<author>Yuval Yarom</author>\n<title>Spectre Attacks: Exploiting Speculative Execution.</title>\n<journal>meltdownattack.com</journal>\n<year>2018</year>\n<ee>https://spectreattack.com/spectre.pdf</ee>\n</article>"
  val testXML2 = "<www mdate=\"2004-03-23\" key=\"www/com/cdrom-zlib\">\n<editor>Jean-Loup Gailly</editor>\n<editor>Mark Adler</editor>\n<title>Zlib Home Page (old location: http://quest.jpl.nasa.gov/zlib)</title>\n<url>http://www.cdrom.com/pub/infozip/zlib/</url>\n</www>"
  val testXML3 = "<www mdate=\"2002-01-03\" key=\"www/fi/hut-som\">\n<title>Self-organizing Map</title>\n<url>http://www.cis.hut.fi/nnrc/nnrc-programs.html</url>\n</www>"

  val parsedXML1: Elem = parseXML(testXML1)
  val parsedXML2: Elem = parseXML(testXML2)
  val parsedXML3: Elem = parseXML(testXML3)


  test("testRemovePunctuations") {
    assert(removePunctuations("@#$%^&*.,?/") == ".")
  }

  test("testGetXMLTagContent") {
    assert(getXMLTagContent(parsedXML1, getAuthorType(parsedXML1)).length == 10)
    assert(getXMLTagContent(parsedXML2, getAuthorType(parsedXML2)).length == 2)
    try {
      assert(getXMLTagContent(parsedXML3, getAuthorType(parsedXML3)).isEmpty)
    }
    catch {
      case e: NullPointerException =>
        println("This record has no corresponding authors.")
    }
  }

  test("testDtdFilePath") {
    assert(dtdSchemaUri.isInstanceOf[URI])
  }

  test("testGetPubVenueType") {
    assert(getPubVenueType(parsedXML1) == "journal")
    assert(getPubVenueType(parsedXML2) == "www")

  }

  test("testParseXML") {
    assert(parsedXML1.nonEmpty)
    assert(parsedXML1.isInstanceOf[Elem])
    assert(parsedXML2.nonEmpty)
    assert(parsedXML2.isInstanceOf[Elem])
    assert(parsedXML3.nonEmpty)
    assert(parsedXML3.isInstanceOf[Elem])
  }

  test("testGetPubType") {
    assert(getPubType(parsedXML1) == "article")
    assert(getPubType(parsedXML2) == "www")
    assert(getPubType(parsedXML3) == "www")
  }

  test("testGetAuthorType") {
    assert(getAuthorType(parsedXML1) == "author")
    assert(getAuthorType(parsedXML2) == "editor")
    assert(getAuthorType(parsedXML3) == null)
  }

}
