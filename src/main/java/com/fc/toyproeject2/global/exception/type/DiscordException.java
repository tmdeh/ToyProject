package com.fc.toyproeject2.global.exception.type;

import com.fc.toyproeject2.global.exception.error.ErrorCode;
import org.springframework.web.client.HttpStatusCodeException;

public class DiscordException extends HttpStatusCodeException {

    public DiscordException(ErrorCode errorCode) {
        super(errorCode.getCode(), errorCode.getInfo());
    }

}
