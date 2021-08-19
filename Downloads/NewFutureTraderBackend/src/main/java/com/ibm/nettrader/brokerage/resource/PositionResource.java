/*
 * (c) 2021 IBM Financial Industry Solutions GmbH, All rights reserved.
 */

package com.ibm.nettrader.brokerage.resource;

import com.ibm.nettrader.brokerage.custom.exceptions.EmptyResponse;
import com.ibm.nettrader.brokerage.payload.PositionsPayloadXml;
import com.ibm.nettrader.brokerage.utilities.CallStoredProcedure;
import com.ibm.nettrader.brokerage.utilities.PosLst;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The Class PositionResource.Used to get all positions.
 * - @author 03186D744
 */
@RestController
public class PositionResource {
  
  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(PositionResource.class);

  /** The call stored procedure. */
  CallStoredProcedure callStoredProcedure = null;
  
  /** The db out xml. */
  String dbOutXml = null;
  
  /** The pos lst. */
  PosLst posLst = null;
  
  /** The positions payload xml. */
  PositionsPayloadXml positionsPayloadXml = null;
  
  /** The positions payload xml string. */
  String positionsPayloadXmlString = null;
   
  /** The data source. */
  @Autowired
  private DataSource dataSource;
  
  /**
   * Gets the all positions.
   *
   * @param positionRequestBody the pos request body
   * @return the all positions
   * @throws Exception the exception
   */
  @PostMapping(value = "/api/positions", produces = MediaType.APPLICATION_JSON_VALUE)
  public String getAllPositions(@RequestBody String positionRequestBody) throws Exception {

    positionsPayloadXml = new PositionsPayloadXml();
    logger.info("Entered into positions endpoint...");
    callStoredProcedure = new CallStoredProcedure(dataSource);
    logger.info("Calling Positions Stored procedure...");
    positionsPayloadXmlString = positionsPayloadXml.payloadXmlString(positionRequestBody);
    dbOutXml = callStoredProcedure.executeStoredProcedure("POSITION", "GET", 
        positionsPayloadXmlString).toString();
    logger.info("Positions Stored procedure executed successfully.");
    posLst = new PosLst(dbOutXml);
    // check posLst is empty or not
    if (posLst == null || posLst.isEmpty()) {
      throw new EmptyResponse("Recieved empty positions from databse");
    }
    return posLst.toString();
  }
}
