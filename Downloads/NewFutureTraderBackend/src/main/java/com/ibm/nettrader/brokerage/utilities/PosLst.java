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
 * The Class PosLst.
 */
public class PosLst extends JSONObject {
    
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
  public PosLst(String backendData) throws ParserConfigurationException {
    super();
    try {
      //parse string xml
      BackendData data = new BackendData(backendData);
      put("offset", data.getIntVal("/oromxml/data/@offset"));
      JSONArray jsonArray = new JSONArray();
      NodeList nodeList = data.getSubDocs("/oromxml/data/PosLst/Pos");
      for (int i = 1; i <= nodeList.getLength(); i++) {
        JSONObject jsonObject = new JSONObject();
        JSONObject contractJson = new JSONObject();
        String path = String.format("/oromxml/data/PosLst/Pos[%d]", i);
        jsonObject.put("Acct", data.getStringVal(String.format("%s/Acct/text()", path)));
        contractJson.put("id", data.getStringVal(String.format("%s/CntrK/text()", path)));
        contractJson.put("symbol", data.getStringVal(String.format("%s/Cntr/text()", path)));
        jsonObject.put("Cntr", contractJson);
        jsonObject.put("Curr", data.getStringVal(String.format("%s/Curr/text()", path)));
        jsonObject.put("LstTrd", data.getStringVal(String.format("%s/LstTrd/text()", path)));
        jsonObject.put("Xch", data.getStringVal(String.format("%s/Xch/text()", path)));
        jsonObject.put("Long", data.getIntVal(String.format("%s/Long/text()", path)));
        jsonObject.put("LDO", data.getIntVal(String.format("%s/LDO/text()", path)));
        jsonObject.put("LDC", data.getIntVal(String.format("%s/LDC/text()", path)));
        jsonObject.put("Short", data.getIntVal(String.format("%s/Short/text()", path)));
        jsonObject.put("SDO", data.getIntVal(String.format("%s/SDO/text()", path)));
        jsonObject.put("SDC", data.getIntVal(String.format("%s/SDC/text()", path)));
        jsonObject.put("PosV", data.getDoubleVal(String.format("%s/PosV/text()", path)));
        jsonObject.put("PnL", data.getDoubleVal(String.format("%s/PnL/text()", path)));
        jsonObject.put("AvgP", data.getDoubleVal(String.format("%s/AvgP/text()", path)));
        jsonObject.put("LstP", data.getDoubleVal(String.format("%s/LstP/text()", path)));
        jsonObject.put("StlP", data.getDoubleVal(String.format("%s/StlP/text()", path)));
        jsonObject.put("AvgFX", data.getDoubleVal(String.format("%s/AvgFX/text()", path)));
        jsonObject.put("LstFX", data.getDoubleVal(String.format("%s/LstFX/text()", path)));
        jsonObject.put("ITM", data.getStringVal(String.format("%s/ITM/text()", path)));
        jsonArray.put(jsonObject);
      }
      put("PosLst", jsonArray);
    } catch (ParserConfigurationException parseEx) {
      parseEx.getMessage();  
    } catch (IOException ioEx) {
      ioEx.getMessage();
    } catch (SAXException saxEx) {
      saxEx.getMessage();
    } catch (JSONException parseEx) {
      parseEx.getMessage();
    } catch (XPathExpressionException xpathEx) {
      xpathEx.getMessage();
    } 
  }
}
