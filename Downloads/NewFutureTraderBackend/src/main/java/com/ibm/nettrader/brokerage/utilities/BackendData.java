/*
 * (c) 2021 IBM Financial Industry Solutions GmbH, All rights reserved.
 */

package com.ibm.nettrader.brokerage.utilities;

import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * The Class BackendData.
 */
public class BackendData {

  /** The builder. */
  private final DocumentBuilder builder;
  
  /** The doc. */
  private final Document doc;

  /** The x path. */
  private final XPath xpath;
  
  /**
  * Instantiates a new backend data.
  *
  * @param backendData the backend data => pass the XML received from database.
  * @throws ParserConfigurationException the parser configuration exception
  * @throws SAXException the SAX exception
  * @throws IOException Signals that an I/O exception has occurred.
  */
  public BackendData(String backendData) throws ParserConfigurationException, 
      SAXException, IOException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    builder = factory.newDocumentBuilder();
    doc = builder.parse(new InputSource(new StringReader(backendData)));
    xpath = XPathFactory.newInstance().newXPath();
  }

  /**
  * Gets the sub docs.
  *
  * @param atPath the at path
  * @return the sub docs
  * @throws XPathExpressionException the x path expression exception
  */
  public NodeList getSubDocs(String atPath) throws XPathExpressionException {
    return (NodeList) xpath.evaluate(atPath, doc, XPathConstants.NODESET);
  }

  /**
  * Gets the string val.
  *
  * @param atPath the at path
  * @return the string val
  * @throws XPathExpressionException the x path expression exception
  */
  public String getStringVal(String atPath) throws XPathExpressionException {
    Node node = (Node) xpath.evaluate(atPath, doc, XPathConstants.NODE);
    return node != null ? node.getTextContent() : null;
  }
  
  /**
  * Gets the int val.
  *
  * @param atPath the at path
  * @return the int val
  * @throws XPathExpressionException the x path expression exception
  */
  public Integer getIntVal(String atPath) throws XPathExpressionException {
    String s = getStringVal(atPath);
    if (s != null) {
      return Integer.parseInt(s);
    } else {
      return null;
    }
  }

  /**
  * Gets the double val.
  *
  * @param atPath the at path
  * @return the double val
  * @throws XPathExpressionException the x path expression exception
  */
  public Double getDoubleVal(String atPath) throws XPathExpressionException {
    String s = getStringVal(atPath);
    if (s != null) {
      return Double.parseDouble(s);
    } else {
      return null;
    }
  }
}
