package com.fc.toyproeject2.global.exception.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum LikeErrorCode implements ErrorCode {
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다"),
  TRIP_NOT_FOUND(HttpStatus.NOT_FOUND, "여행을 찾을 수 없습니다"),
  LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "좋아요를 찾을 수 없습니다"),
  NO_LIKED_TRIPS(HttpStatus.NOT_FOUND, "좋아요를 누른 여행이 없습니다");

  private final HttpStatusCode code;
  private final String info;

  LikeErrorCode(HttpStatusCode code, String info) {
    this.code = code;
    this.info = info;
  }

  @Override
  public HttpStatusCode getCode() {
    return this.code;
  }

  @Override
  public String getInfo() {
    return this.info;
  }
}
