/*
 * (c) 2021 IBM Financial Industry Solutions GmbH, All rights reserved.
 */

package com.ibm.nettrader.brokerage.resource;

import com.ibm.nettrader.brokerage.custom.exceptions.EmptyResponse;
import com.ibm.nettrader.brokerage.payload.AccountPayloadXml;
import com.ibm.nettrader.brokerage.utilities.AcctLst;
import com.ibm.nettrader.brokerage.utilities.CallStoredProcedure;
import javax.sql.DataSource;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class is used to to get account details.
 * - @author 03186D744
 * 
 */
@RestController 
public class AccountResource {
  
  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(AccountResource.class);
  
  /** The call stored procedure. */
  CallStoredProcedure callStoredProcedure = null;
  
  /** The account payload xml. */
  AccountPayloadXml accountPayloadXml = null;
  
  /** The account payload xml string. */
  String  accountPayloadXmlString = null;
  
  AcctLst acctLst = null;
  
  /** The db out xml. */
  String dbOutXml = null;
  
  /** The data source. */
  @Autowired
  private DataSource dataSource;
  
  /**
   * Gets the account.
   *
   * @param acct the acct
   * @return the account
   * @throws Exception the exception
   * @throws JSONException the JSON exception
   */
  @GetMapping(value = "/api/account/{acct}", produces = MediaType.APPLICATION_JSON_VALUE)
  public String getAccount(@PathVariable String acct) throws Exception {
    
    accountPayloadXml = new AccountPayloadXml();
    logger.info("Entered into Account endpoint...");
    callStoredProcedure = new CallStoredProcedure(dataSource);
    logger.info("Calling Account Stored procedure...");
    accountPayloadXmlString = accountPayloadXml.payloadXmlString(acct);
    dbOutXml = callStoredProcedure.executeStoredProcedure("ACCOUNT", "GET", 
        accountPayloadXmlString).toString();
    logger.info("Account Stored procedure executed successfully.");
    acctLst = new AcctLst(dbOutXml);
    // check outXML is empty or not
    if (acctLst == null || acctLst.isEmpty()) {
      throw new EmptyResponse("Recieved empty account from databse");
    }
    return acctLst.toString();
  }
}
