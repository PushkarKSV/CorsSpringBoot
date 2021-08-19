/*
 * (c) 2021 IBM Financial Industry Solutions GmbH, All rights reserved.
 */

package com.ibm.nettrader.brokerage.custom.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The Class EmptyResponse.
 * - @author 03186D744
 */
@ResponseStatus(HttpStatus.NO_CONTENT)
public class EmptyResponse extends RuntimeException {
  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 6893393089082010869L;
  /**
  * Instantiates a new empty response from DB.
  *
  * @param message the message
  */
  
  public EmptyResponse(String message) {
    super(message);
  }
}
