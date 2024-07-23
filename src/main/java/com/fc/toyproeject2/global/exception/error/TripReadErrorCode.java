package com.fc.toyproeject2.global.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum TripReadErrorCode implements ErrorCode{
    NOT_FOUND_TRIP(HttpStatusCode.valueOf(404), "해당 여행은 존재하지 않습니다."),
    ;
    private final HttpStatusCode code;
    private final String info;
}
