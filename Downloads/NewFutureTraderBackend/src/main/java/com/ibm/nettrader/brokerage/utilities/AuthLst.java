/*
 * (c) 2021 IBM Financial Industry Solutions GmbH, All rights reserved.
 */

package com.ibm.nettrader.brokerage.utilities;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

/**
 * The Class AcctLst.
 */
public class AuthLst extends JSONObject {
    
  /**
   * Instantiates a new Auth data.
   *
   * @param backendData the backend data
   * @throws ParserConfigurationException the parser configuration exception
   * @throws JSONException the JSON exception
   */
  public AuthLst(String backendData) throws ParserConfigurationException {
    super();
    try {
      //parse string xml
      BackendData data = new BackendData(backendData);
      //NodeList nodeList = data.getSubDocs("/oromxml/data/Acct");
      JSONObject jsonObject = new JSONObject();
      String path = String.format("/oromxml/data/session_info");
      jsonObject.put("session_credential", data.getStringVal(String.format(
          "%s/session_credential/text()", path)));
      jsonObject.put("account_no", data.getStringVal(String.format("%s/account_no/text()", path)));
      jsonObject.put("cash_account_no", data.getStringVal(String.format(
          "%s/cash_account_no/text()", path)));
      jsonObject.put("authorization_no", data.getStringVal(String.format(
          "%s/authorization_no/text()", path)));
      put("Auth", jsonObject);    
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
