/*
 * (c) 2021 IBM Financial Industry Solutions GmbH, All rights reserved.
 */

package com.ibm.nettrader.brokerage.resource;

import com.ibm.nettrader.brokerage.custom.exceptions.EmptyResponse;
import com.ibm.nettrader.brokerage.payload.AuthPayloadXml;
import com.ibm.nettrader.brokerage.utilities.AuthLst;
import com.ibm.nettrader.brokerage.utilities.CallStoredProcedure;
import javax.sql.DataSource;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class is used to authenticate a application.
 * - @author 03186D744
 * 
 */
@RestController 
public class AuthResource {
  
  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(AuthResource.class);
  
  /** The call stored procedure. */
  CallStoredProcedure callStoredProcedure = null;
  
  /** The auth payload xml. */
  AuthPayloadXml authPayloadXml = null;
  
  /** The auth payload xml string. */
  String  authPayloadXmlString = null;
  
  /** The db out xml. */
  String dbOutXml = null;
  
  AuthLst authLst = null;
  
  /** The data source. */
  @Autowired
  private DataSource dataSource;
  
  /**
   * login to the the app.
   *
   * @param authRequestBody the auth request body
   * @return the account
   * @throws Exception the exception
   * @throws JSONException the JSON exception
   */
  @PutMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
  public String loginApp(@RequestBody String authRequestBody) throws Exception {

    logger.info("Entered into auth endpoint...");
    authPayloadXml = new AuthPayloadXml();
    callStoredProcedure = new CallStoredProcedure(dataSource);
    authPayloadXmlString = authPayloadXml.payloadXmlString(authRequestBody);
    dbOutXml = callStoredProcedure.executeStoredProcedure("AUTH", "PUT", 
        authPayloadXmlString).toString();
    logger.info("Auth Stored procedure executed successfully.");
    authLst = new AuthLst(dbOutXml);
    // check outXML is empty or not
    if (authLst == null || authLst.isEmpty()) {
      throw new EmptyResponse("Recieved empty auth details from databse");
    }
    return authLst.toString();
  }
}
