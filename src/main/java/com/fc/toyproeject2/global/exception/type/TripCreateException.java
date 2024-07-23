package com.fc.toyproeject2.global.exception.type;

import com.fc.toyproeject2.global.exception.error.TripCreateErrorCode;
import org.springframework.web.client.HttpStatusCodeException;

public class TripCreateException extends HttpStatusCodeException {

  public TripCreateException(TripCreateErrorCode errorCode) {
    super(errorCode.getCode(), errorCode.getInfo());
  }
}