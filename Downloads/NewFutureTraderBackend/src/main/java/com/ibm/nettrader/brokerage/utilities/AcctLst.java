/*
 * (c) 2021 IBM Financial Industry Solutions GmbH, All rights reserved.
 */

package com.ibm.nettrader.brokerage.utilities;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * The Class AcctLst.
 */
public class AcctLst extends JSONObject {
    
  /**
   * Instantiates a new acct data.
   *
   * @param backendData the backend data
   * @throws ParserConfigurationException the parser configuration exception
   * @throws JSONException the JSON exception
   */
  public AcctLst(String backendData) throws ParserConfigurationException {
    super();
    try {
      //parse string xml
      BackendData data = new BackendData(backendData);
      //NodeList nodeList = data.getSubDocs("/oromxml/data/Acct");
      JSONObject jsonObject = new JSONObject();
      String path = String.format("/oromxml/data/Acct");
      jsonObject.put("id", data.getStringVal(String.format("%s/@id", path)));
      jsonObject.put("CrdL", data.getStringVal(String.format("%s/CrdL/text()", path)));
      jsonObject.put("TotObl", data.getStringVal(String.format("%s/TotObl/text()", path)));
      jsonObject.put("Bal", data.getStringVal(String.format("%s/Bal/text()", path)));
      jsonObject.put("CRat", data.getStringVal(String.format("%s/CRat/text()", path)));
      jsonObject.put("DrvBal", data.getStringVal(String.format("%s/DrvBal/text()", path)));
      jsonObject.put("DrvIL", data.getStringVal(String.format("%s/DrvIL/text()", path)));
      jsonObject.put("MrgL", data.getStringVal(String.format("%s/MrgL/text()", path)));
      jsonObject.put("AssMrg", data.getStringVal(String.format("%s/AssMrg/text()", path)));
      jsonObject.put("MrgObl", data.getStringVal(String.format("%s/MrgObl/text()", path)));
      jsonObject.put("MrgC", data.getStringVal(String.format("%s/MrgC/text()", path)));
      jsonObject.put("CashC", data.getStringVal(String.format("%s/CashC/text()", path)));
      jsonObject.put("PrLB", data.getStringVal(String.format("%s/PrLB/text()", path)));
      jsonObject.put("PrRec", data.getStringVal(String.format("%s/PrRec/text()", path)));
      jsonObject.put("PrP", data.getStringVal(String.format("%s/PrP/text()", path)));
      jsonObject.put("VM", data.getStringVal(String.format("%s/VM/text()", path)));
      jsonObject.put("Owner", data.getStringVal(String.format("%s/Owner/text()", path)));
      put("Acct", jsonObject);    
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
