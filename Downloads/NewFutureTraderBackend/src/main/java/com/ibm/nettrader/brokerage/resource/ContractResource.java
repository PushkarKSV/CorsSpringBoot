/*
 * (c) 2021 IBM Financial Industry Solutions GmbH, All rights reserved.
 */

package com.ibm.nettrader.brokerage.resource;

import com.ibm.nettrader.brokerage.custom.exceptions.EmptyResponse;
import com.ibm.nettrader.brokerage.payload.AccountPayloadXml;
import com.ibm.nettrader.brokerage.payload.ContractPayloadXml;
import com.ibm.nettrader.brokerage.utilities.AcctLst;
import com.ibm.nettrader.brokerage.utilities.CallStoredProcedure;
import com.ibm.nettrader.brokerage.utilities.CntrLst;

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
 * This class is used to to get contract details.
 * - @author 03186D744
 * 
 */
@RestController 
public class ContractResource {
  
  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(ContractResource.class);
  
  /** The call stored procedure. */
  CallStoredProcedure callStoredProcedure = null;
  
  /** The account payload xml. */
  ContractPayloadXml contractPayloadXml = null;
  
  /** The account payload xml string. */
  String  contractPayloadXmlString = null;
  
  CntrLst cntrLst = null;
  
  /** The db out xml. */
  String dbOutXml = null;
  
  /** The data source. */
  @Autowired
  private DataSource dataSource;
  
  /**
   * Gets the contract.
   *
   * @param cntr the cntr
   * @return the contract
   * @throws Exception the exception
   * @throws JSONException the JSON exception
   */
  @GetMapping(value = "/api/contract/{cntrk}", produces = MediaType.APPLICATION_JSON_VALUE)
  public String getAccount(@PathVariable String cntrk) throws Exception {
    
    contractPayloadXml = new ContractPayloadXml();
    logger.info("Entered into Contract endpoint...");
    callStoredProcedure = new CallStoredProcedure(dataSource);
    logger.info("Calling Contract Stored procedure...");
    contractPayloadXmlString = contractPayloadXml.payloadXmlString(cntrk);
    dbOutXml = callStoredProcedure.executeStoredProcedure("CONTRACT", "GET", 
        contractPayloadXmlString).toString();
    logger.info("Contract Stored procedure executed successfully.");
    cntrLst = new CntrLst(dbOutXml);
    // check outXML is empty or not
    if (cntrLst == null || cntrLst.isEmpty()) {
      throw new EmptyResponse("Recieved empty contract from databse");
    }
    return cntrLst.toString();
  }
}
