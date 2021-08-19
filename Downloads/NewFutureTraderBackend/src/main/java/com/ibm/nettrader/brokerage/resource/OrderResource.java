/*
 * (c) 2021 IBM Financial Industry Solutions GmbH, All rights reserved.
 */

package com.ibm.nettrader.brokerage.resource;

import com.ibm.nettrader.brokerage.custom.exceptions.EmptyResponse;
import com.ibm.nettrader.brokerage.payload.OrderPayloadXml;
import com.ibm.nettrader.brokerage.utilities.CallStoredProcedure;
import com.ibm.nettrader.brokerage.utilities.OrdLst;
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
public class OrderResource {
  
  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(OrderResource.class);

  /** The call stored procedure. */
  CallStoredProcedure callStoredProcedure = null;
  
  /** The db out xml. */
  String dbOutXml = null;
  
  /** The pos lst. */
  OrdLst ordLst = null;
  
  /** The order payload xml. */
  OrderPayloadXml orderPayloadXml = null;
  
  /** The order payload xml string. */
  String orderPayloadXmlString = null;
  
  /** The data source. */
  @Autowired
  private DataSource dataSource;
  
  /**
   * Gets the OrderList records.
   *
   * @param orderListRequestBody the order request body
   * @return the all positions
   * @throws Exception the exception
   */
  @PostMapping(value = "/api/orders",  produces = MediaType.APPLICATION_JSON_VALUE)
  public String getOrderList(@RequestBody String orderListRequestBody) throws Exception {
    
    logger.info("Entered into OrderList endpoint...");
    orderPayloadXml = new OrderPayloadXml();       
    callStoredProcedure = new CallStoredProcedure(dataSource);
    logger.info("Calling OrderList Stored procedure...");
    orderPayloadXmlString = orderPayloadXml.payloadXmlString(orderListRequestBody);
    dbOutXml = callStoredProcedure.executeStoredProcedure("ORDER", "GET", 
      orderPayloadXmlString).toString();
    logger.info("OrderList Stored procedure executed successfully.");
    ordLst = new OrdLst(dbOutXml);
    // check posLst is empty or not
    if (ordLst == null || ordLst.isEmpty()) {
      throw new EmptyResponse("Recieved empty orderList from database");
    }
    return ordLst.toString();
  }
}
