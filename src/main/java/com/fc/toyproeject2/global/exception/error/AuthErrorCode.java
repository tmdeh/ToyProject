package com.fc.toyproeject2.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 사용자는 없습니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료됐습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다."),
    LOGIN_FAILED(HttpStatus.BAD_REQUEST, "로그인에 실패하였습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    ERROR_SEND_EMAIL(HttpStatus.BAD_REQUEST, "이메일 전송에 실패하였습니다."),
    WRONG_EMAIL_SUCCESS_KEY(HttpStatus.BAD_REQUEST, "이메일 인증코드가 맞지 않습니다.")
    ;

    private final HttpStatusCode code;
    private final String info;
}
