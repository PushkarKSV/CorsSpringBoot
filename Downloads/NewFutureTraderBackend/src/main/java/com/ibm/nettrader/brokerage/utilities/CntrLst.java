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
 * The Class CntrLst.
 */
public class CntrLst extends JSONObject {
    
  /**
   * Instantiates a new acct data.
   *
   * @param backendData the backend data
   * @throws ParserConfigurationException the parser configuration exception
   * @throws JSONException the JSON exception
   */
  public CntrLst(String backendData) throws ParserConfigurationException {
    super();
    try {
      //parse string xml
      BackendData data = new BackendData(backendData);
      JSONObject jsonObject = new JSONObject();
      String path = String.format("/oromxml/data/Cntr");
      jsonObject.put("id", data.getStringVal(String.format("%s/@id", path)));
      jsonObject.put("symbol", data.getStringVal(String.format("%s/Symbol/text()", path)));
      jsonObject.put("Dcml", data.getIntVal(String.format("%s/Dcml/text()", path)));
      jsonObject.put("TicS", data.getDoubleVal(String.format("%s/TicS/text()", path)));
      jsonObject.put("CovI", data.getStringVal(String.format("%s/CovI/text()", path)));
      JSONArray jsonArray = new JSONArray();
      NodeList nodeList = data.getSubDocs("/oromxml/data/Cntr/XchL");
      for (int i = 1; i <= nodeList.getLength(); i++) {
        JSONObject xchlJson = new JSONObject();
        String xchlPath = String.format("/oromxml/data/Cntr/XchL[%d]", i);
        xchlJson.put("id", data.getStringVal(String.format("%s/Xch/@id", xchlPath)));
        xchlJson.put("name", data.getStringVal(String.format("%s/Xch/text()", xchlPath)));
        jsonArray.put(jsonObject.put("XchL", xchlJson));
      }
      put("Cntr", jsonObject);    
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
