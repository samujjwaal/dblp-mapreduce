package com.samujjwaal.hw2.util

import java.net.URI

import scala.xml.{Elem, XML}

object MapReduceUtils {

  val dtdSchemaUri: URI = getClass.getClassLoader.getResource("dblp.dtd").toURI

  /**
   * To parse a XML record using the dblp.dtd schema
   */
  def parseXML(xmlBlock: String): Elem = {
    val xmlToParse = s"""<?xml version="1.0" encoding="ISO-8859-1"?><!DOCTYPE dblp SYSTEM "$dtdSchemaUri"><dblp>""" + xmlBlock + "</dblp>"
    //value holds only one publication record at a time
    XML.loadString(xmlToParse)
  }

  /**
   * Get the author type of a XML record
   */
  def getAuthorType(xmlBlock: Elem): String = getPubType(xmlBlock) match {
    case "proceedings" => "editor"
    case "www" =>
      if (getXMLTagContent(xmlBlock, "author").nonEmpty)
        "author"
      else if (getXMLTagContent(xmlBlock, "editor").nonEmpty)
        "editor"
      else
        null
    case _ => "author"
  }

  /**
   * Extract all the values corresponding to the XML tag denoted by <xmlTag> for a block of XML text xmlBlock
   */
  def getXMLTagContent(xmlBlock: Elem, xmlTag: String): List[String] =
    (xmlBlock \\ xmlTag).map(content => content.text.toLowerCase.trim).toList

  /**
   * Get the venue of publication of a XML record
   */
  def getPubVenueType(xmlBlock: Elem): String = getPubType(xmlBlock) match {
    case "article" => "journal"
    case "book" => "publisher"
    case "phdthesis" => "school"
    case "mastersthesis" => "school"
    case "incollection" => "booktitle"
    case "inproceedings" => "booktitle"
    case "proceedings" => "booktitle"
    case "www" => "www"
  }

  /**
   * Get the type of publication of a XML record
   */
  def getPubType(xmlBlock: Elem): String = xmlBlock.child.head.label

  /**
   * Remove punctuations from a string
   */
  def removePunctuations(text: String): String = text.replaceAll("""[\p{Punct}&&[^.]]""", "")

}