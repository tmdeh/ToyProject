package com.fc.toyproeject2.global.exception.error;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum TripUpdateErrorCode {
    TRIP_NOT_FOUND(HttpStatusCode.valueOf(404), "여행이 존재하지 않습니다.")
    ;
    private final HttpStatusCode code;
    private final String info;
}
