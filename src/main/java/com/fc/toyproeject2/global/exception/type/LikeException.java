package com.fc.toyproeject2.global.exception.type;

import com.fc.toyproeject2.global.exception.error.ErrorCode;
import org.springframework.web.client.HttpStatusCodeException;

public class LikeException extends HttpStatusCodeException {
  private final ErrorCode errorCode;

  public LikeException(ErrorCode errorCode) {
    super(errorCode.getCode(), errorCode.getInfo());
    this.errorCode = errorCode;
  }


}
