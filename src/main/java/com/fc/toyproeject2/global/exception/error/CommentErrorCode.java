package com.fc.toyproeject2.global.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum CommentErrorCode {
    COMMENT_NOT_FOUND(HttpStatusCode.valueOf(404), "댓글이 존재하지 않습니다."),
    USER_NOT_FOUND(HttpStatusCode.valueOf(404), "유저가 존재하지 않습니다."),
    Forbidden(HttpStatusCode.valueOf(403), "권한이 없습니다.");
    ;
    private final HttpStatusCode code;
    private final String info;
}
