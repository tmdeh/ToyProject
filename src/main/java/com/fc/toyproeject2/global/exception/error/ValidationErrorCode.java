package com.fc.toyproeject2.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;

@Getter
@RequiredArgsConstructor
public enum ValidationErrorCode implements ErrorCode{
    NOT_FOUND_USER(HttpStatusCode.valueOf(400), "해당 유저를 찾을수 없습니다."),
    NOT_FOUND_OBJECT(HttpStatusCode.valueOf(400), "해당 정보를 찾을수 없습니다."),
    NOT_MASTER_USER(HttpStatusCode.valueOf(400), "자신이 만든 여행이 아닙니다."),
    NOT_START_TIME_BEFORE_END_TIME(HttpStatusCode.valueOf(400), "시작 시간이 종료 시간보다 늦으면 안됩니다."),
    NOT_START_DATE_BEFORE_END_DATE(HttpStatusCode.valueOf(400), "시작 날짜가 끝나는 날짜보다 늦으면 안됩니다."),
    INVALID_PLACE_NAME(HttpStatusCode.valueOf(400), "유효하지 않은 장소 이름입니다.");

    private final HttpStatusCode code;
    private final String info;
}
