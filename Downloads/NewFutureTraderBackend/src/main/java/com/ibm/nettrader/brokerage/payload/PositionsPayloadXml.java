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
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * The Class PayloadXml.
 * - @author 03186D744
 */
public class PositionsPayloadXml {
  
  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(PositionsPayloadXml.class);

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
  public String payloadXmlString(String positionRequestBody) {
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
      
      Object document = Configuration.defaultConfiguration().jsonProvider()
          .parse(positionRequestBody);
      
      // setting attribute offset to integer
      Attr attrOffset = doc.createAttribute("offset");
      attrOffset.setValue(JsonPath.read(document, "$.filter.offset").toString());
      dataElement.setAttributeNode(attrOffset);
      
      // setting attribute rows to integer
      Attr attrRows = doc.createAttribute("rows");
      attrRows.setValue(JsonPath.read(document, "$.filter.rows").toString());
      dataElement.setAttributeNode(attrRows);
      
      // filter element
      Element filterElement = doc.createElement("filter");
      dataElement.appendChild(filterElement);
     
      JSONObject object = new JSONObject(positionRequestBody);
      JSONObject getFilterObject = object.getJSONObject("filter");
      JSONArray getCritArray = getFilterObject.getJSONArray("crit");
      
      for (int i = 0; i < getCritArray.length(); i++) {

        // crit element
        Element critElement = doc.createElement("crit");
        filterElement.appendChild(critElement);

        // setting value to attr  for eg .ACCT, CNTR, etc...
        Attr attrAcct = doc.createAttribute("attr");
        attrAcct.setValue(JsonPath.read(document, "$.filter.crit[" + i + "].attr"));
        critElement.setAttributeNode(attrAcct);
      
        // setting attribute op to value
        Attr attrOp = doc.createAttribute("op");
        attrOp.setValue(JsonPath.read(document, "$.filter.crit[" + i + "].op"));
        critElement.setAttributeNode(attrOp);
      
        // val element
        Element valElement = doc.createElement("val");
        valElement.appendChild(doc.createTextNode(JsonPath.read(document, 
            "$.filter.crit[" + i + "].val")));
        critElement.appendChild(valElement);
      }
      // sort element
      Element sortElement = doc.createElement("sort");
      dataElement.appendChild(sortElement);
      
      JSONObject getSortObject = object.getJSONObject("sort");
      
      for (int i = 0; i < getSortObject.length(); i++) {
      
        // setting attribute attr to by column
        Attr attrBy = doc.createAttribute("by");
        attrBy.setValue(JsonPath.read(document, "$.sort.by"));
        sortElement.setAttributeNode(attrBy);
      
        // setting attribute order to asc or desc
        Attr attrOrder = doc.createAttribute("order");
        attrOrder.setValue(JsonPath.read(document, "$.sort.order"));
        sortElement.setAttributeNode(attrOrder);
      
      }
      // write the content into xml
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
         
      // write the content into String
      StringWriter writer = new StringWriter();
      transformer.transform(new DOMSource(doc), new StreamResult(writer));
      payloadString = writer.getBuffer().toString();
      logger.debug(this.getClass().getSimpleName() + ": \n" + payloadString);  
    } catch (ParserConfigurationException ex) {
      logger.debug("XML is not well formed, :" + ex.getMessage());
    } catch (TransformerException ex) {
      logger.debug("XML is not transformed :" + ex.getMessage());
    }
    return payloadString;
  }
}
