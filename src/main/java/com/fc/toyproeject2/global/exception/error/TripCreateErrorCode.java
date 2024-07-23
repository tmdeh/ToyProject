package com.fc.toyproeject2.global.exception.error;
import org.springframework.http.HttpStatus;

public enum TripCreateErrorCode implements ErrorCode {
  DUPLICATE_TRIP_NAME(HttpStatus.BAD_REQUEST, "이미 존재하는 여행 이름입니다."),
  EMPTY_TRIP_DATA(HttpStatus.BAD_REQUEST, "입력 데이터가 비어있습니다. 여행 이름, 날짜는 모두 입력 해주세요");

  private final HttpStatus code;
  private final String info;

  TripCreateErrorCode(HttpStatus code, String info) {
    this.code = code;
    this.info = info;
  }

  @Override
  public HttpStatus getCode() {
    return code;
  }

  @Override
  public String getInfo() {
    return info;
  }
}