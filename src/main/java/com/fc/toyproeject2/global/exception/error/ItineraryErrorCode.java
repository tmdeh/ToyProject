package com.fc.toyproeject2.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@RequiredArgsConstructor
public enum ItineraryErrorCode implements ErrorCode{
    NOT_FOUND_TRIP_ID(HttpStatusCode.valueOf(404), "Trip ID가 존재하지 않습니다."),
    NOT_FOUND_BASE_CAMP_ID(HttpStatusCode.valueOf(404), "Base Camp ID가 존재하지 않습니다."),
    NOT_FOUND_MOVE_ID(HttpStatusCode.valueOf(404), "Move ID가 존재하지 않습니다."),
    NOT_FOUND_STAY_ID(HttpStatusCode.valueOf(404), "Stay ID가 존재하지 않습니다."),
    EMPTY_ITINERARY_DATA(HttpStatus.BAD_REQUEST, "입력 데이터가 비어있습니다. 모두 입력 해주세요")
    ;
    private final HttpStatusCode code;
    private final String info;
}
