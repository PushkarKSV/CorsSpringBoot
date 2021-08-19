/*
 * (c) 2021 IBM Financial Industry Solutions GmbH, All rights reserved.
 */

package com.ibm.nettrader.brokerage.custom.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The Class NormalDataBaseException.
 * - @author 03186D744
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NormalDataBaseException extends RuntimeException {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new normal data base exception.
   *
   * @param message the message
   */
  public NormalDataBaseException(String message) {
    super(message);
  }

}
