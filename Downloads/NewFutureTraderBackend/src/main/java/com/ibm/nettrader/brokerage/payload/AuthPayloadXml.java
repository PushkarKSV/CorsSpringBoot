/*
 * (c) 2021 IBM Financial Industry Solutions GmbH, All rights reserved.
 */

package com.ibm.nettrader.brokerage.payload;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * The Class AuthPayloadXml.
 * - @author 03186D744
 */
public class AuthPayloadXml {
  
  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(AuthPayloadXml.class);

  /** The ts rq rcvd. */
  String tsRqRcvd = null;

  /** The ts rq svc call. */
  String tsRqSvcCall = null;

  /** The simple date format. */
  SimpleDateFormat simpleDateFormat = null;

  /** The payload string. */
  String payloadString = null;
  
  /** The dbuilder. */
  DocumentBuilder dbuilder = null;
  
  /** The doc. */
  Document doc = null;
  
  /**
  * Payload xml string.
  *
  * @return the string
  */
  public String payloadXmlString(String authRequestBody) {
    // DateFormat
    simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSS+0.00");
    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

    // get timestamps
    tsRqRcvd = simpleDateFormat.format(new Date());
    tsRqSvcCall = simpleDateFormat.format(new Date());
        
    try {
      DocumentBuilderFactory dbFactory   = DocumentBuilderFactory.newInstance();
      dbuilder = dbFactory.newDocumentBuilder();
      doc = dbuilder.newDocument();
      
      // root oromxml element
      Element rootElement = doc.createElement("oromxml");
      doc.appendChild(rootElement);
        
      // meta element
      Element metaData = doc.createElement("meta");
      rootElement.appendChild(metaData);
        
      // perf element
      Element perfElement = doc.createElement("perf");
      metaData.appendChild(perfElement);
        
      // setting attribute rq_id to element
      Attr attrRqId = doc.createAttribute("rq_id");
      attrRqId.setValue(UUID.randomUUID().toString());
      perfElement.setAttributeNode(attrRqId);
      
      // setting attribute ts_rq_rcvd to element
      Attr attrTsRqRcvd = doc.createAttribute("ts_rq_rcvd");
      attrTsRqRcvd.setValue(tsRqRcvd);
      perfElement.setAttributeNode(attrTsRqRcvd);
      
      // setting attribute ts_rq_svc_call to element
      Attr attrTsRqSvcCall = doc.createAttribute("ts_rq_svc_call");
      attrTsRqSvcCall.setValue(tsRqSvcCall);
      perfElement.setAttributeNode(attrTsRqSvcCall);
      
      // data element
      Element dataElement = doc.createElement("data");
      rootElement.appendChild(dataElement);
      
      // auth element
      Element authElement = doc.createElement("auth");
      dataElement.appendChild(authElement);
      
      Object document = Configuration.defaultConfiguration().jsonProvider().parse(authRequestBody);
      
      // setting attribute method to login
      Attr attrMethod = doc.createAttribute("method");
      attrMethod.setValue(JsonPath.read(document, "$.method"));
      authElement.setAttributeNode(attrMethod);
      
      // perf element
      Element credElement = doc.createElement("credentials");
      authElement.appendChild(credElement);
      
      // setting attribute account-no to account_number
      Attr attrAcct = doc.createAttribute("account_no");
      attrAcct.setValue(JsonPath.read(document, "$.accountNo"));
      credElement.setAttributeNode(attrAcct);
      
      // setting attribute pin-no to pin
      Attr attrPin = doc.createAttribute("pin");
      attrPin.setValue(JsonPath.read(document, "$.password"));
      credElement.setAttributeNode(attrPin);
      
      // write the content into xml
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
         
      // write the content into String
      StringWriter writer = new StringWriter();
      transformer.transform(new DOMSource(doc), new StreamResult(writer));
      payloadString = writer.getBuffer().toString();
      logger.info(this.getClass().getSimpleName() + ": \n" + payloadString);  
    } catch (ParserConfigurationException ex) {
      logger.info("XML is not well formed, :" + ex.getMessage());
    } catch (TransformerException ex) {
      logger.info("XML is not transformed :" + ex.getMessage());
    }
    return payloadString;
  }
}
