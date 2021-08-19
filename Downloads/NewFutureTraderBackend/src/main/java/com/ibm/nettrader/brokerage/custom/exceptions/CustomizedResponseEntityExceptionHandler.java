/*
 * (c) 2021 IBM Financial Industry Solutions GmbH, All rights reserved.
 */

package com.ibm.nettrader.brokerage.custom.exceptions;

import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * The Class CustomizedResponseEntityExceptionHandler.
 * - @author 03186D744
 */
@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
  /**
  * Default exception.
  *
  * @param ex the ex
  * @param req the req
  * @return the response entity
  */
  @ExceptionHandler(Exception.class)
  public final ResponseEntity<ExceptionResponse> defaultException(Exception ex, WebRequest req) {
    ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(),
        req.getDescription(false));
    return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
  
  /**
   * Tech db exception.
   *
   * @param ex the ex
   * @param req the req
   * @return the response entity
   */  
  @ExceptionHandler(TechnicalDataBaseException.class)
  public final ResponseEntity<ExceptionResponse> techDbException(
      TechnicalDataBaseException ex, WebRequest req) {
    ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(),
        req.getDescription(false));
    return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
  }
  
  /**
   * Handle normal data base exception.
   *
   * @param ex the ex
   * @param req the req
   * @return the response entity
   */
  @ExceptionHandler(NormalDataBaseException.class)
  public final ResponseEntity<ExceptionResponse> handleNormalDataBaseException(
      NormalDataBaseException ex, WebRequest req) {
    ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(),
        req.getDescription(false));
    return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
  }
  
  /**
   * Handle empty response exception.
   *
   * @param ex the ex
   * @param req the req
   * @return the response entity
   */
  @ExceptionHandler(EmptyResponse.class)
  public final ResponseEntity<ExceptionResponse> handleEmptyResponseException(
      EmptyResponse ex, WebRequest req) {
    ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(),
        req.getDescription(false));
    return new ResponseEntity<>(exceptionResponse, HttpStatus.NO_CONTENT);
  }
}
