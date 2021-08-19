/*
 * (c) 2021 IBM Financial Industry Solutions GmbH, All rights reserved.
 */

package com.ibm.nettrader.brokerage.utilities;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * The Class OrderLst.
 */
public class OrdLst extends JSONObject {
    
  /**
   * Instantiates a new pos lst.
   *
   * @param backendData the backend data
   * @throws ParserConfigurationException the parser configuration exception
   * @throws SAXException the SAX exception
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws JSONException the JSON exception
   * @throws XPathExpressionException the x path expression exception
   */
  public OrdLst(String backendData) throws ParserConfigurationException {
    super();
    try {
      //parse string xml
      BackendData data = new BackendData(backendData);
      put("offset", data.getIntVal("/oromxml/data/@offset"));
      JSONArray jsonArray = new JSONArray();
      NodeList nodeList = data.getSubDocs("/oromxml/data/OrdLst/Ord");
      for (int i = 1; i <= nodeList.getLength(); i++) {
        JSONObject jsonObject = new JSONObject();
        String path = String.format("/oromxml/data/OrdLst/Ord[%d]", i);
        jsonObject.put("id", data.getIntVal(String.format("%s/@id", path)));
        jsonObject.put("Acct", data.getStringVal(String.format("%s/Acct/text()", path)));
        jsonObject.put("CntrK", data.getIntVal(String.format("%s/CntrK/text()", path)));
        jsonObject.put("Cntr", data.getStringVal(String.format("%s/Cntr/text()", path)));
        jsonObject.put("Stat", data.getStringVal(String.format("%s/Stat/text()", path)));
        jsonObject.put("BSInd", data.getStringVal(String.format("%s/BSInd/text()", path)));
        jsonObject.put("OCInd", data.getStringVal(String.format("%s/OCInd/text()", path)));
        jsonObject.put("Qty", data.getDoubleVal(String.format("%s/Qty/text()", path)));
        jsonObject.put("LmtT", data.getStringVal(String.format("%s/LmtT/text()", path)));
        jsonObject.put("Lmt", data.getDoubleVal(String.format("%s/Lmt/text()", path)));
        jsonObject.put("Curr", data.getStringVal(String.format("%s/Curr/text()", path)));
        jsonObject.put("StpL", data.getDoubleVal(String.format("%s/StpL/text()", path)));
        jsonObject.put("QtyD", data.getDoubleVal(String.format("%s/QtyD/text()", path)));
        jsonObject.put("AvgP", data.getDoubleVal(String.format("%s/AvgP/text()", path)));
        jsonObject.put("Exp", data.getStringVal(String.format("%s/Exp/text()", path)));
        jsonObject.put("ExpD", data.getStringVal(String.format("%s/ExpD/text()", path)));
        jsonObject.put("CovInd", data.getStringVal(String.format("%s/CovInd/text()", path)));
        jsonObject.put("OrdTyp", data.getStringVal(String.format("%s/OrdTyp/text()", path)));
        jsonObject.put("OAO", data.getStringVal(String.format("%s/OAO/text()", path)));
        jsonObject.put("Rstr", data.getStringVal(String.format("%s/Rstr/text()", path)));
        jsonObject.put("LstC", data.getIntVal(String.format("%s/LstC/text()", path)));
        jsonArray.put(jsonObject);
      }
      put("OrdLst", jsonArray);
    } catch (ParserConfigurationException parseEx) {
      parseEx.getStackTrace();  
    } catch (IOException ioEx) {
      ioEx.getStackTrace();
    } catch (SAXException saxEx) {
      saxEx.getStackTrace();
    } catch (JSONException parseEx) {
      parseEx.getStackTrace();
    } catch (XPathExpressionException xpathEx) {
      xpathEx.getStackTrace();
    }
  }
}
