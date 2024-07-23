package com.fc.toyproeject2.global.exception.type;

import com.fc.toyproeject2.global.exception.error.CommentErrorCode;
import org.springframework.web.client.HttpStatusCodeException;

public class CommentException extends HttpStatusCodeException {
    public CommentException(CommentErrorCode errorCode) {
        super(errorCode.getCode(), errorCode.getInfo());
    }
}
