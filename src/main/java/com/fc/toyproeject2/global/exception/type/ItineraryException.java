package com.fc.toyproeject2.global.exception.type;

import com.fc.toyproeject2.global.exception.error.ErrorCode;
import org.springframework.web.client.HttpStatusCodeException;

public class ItineraryException extends HttpStatusCodeException {

    public ItineraryException(ErrorCode errorCode) {
        super(errorCode.getCode(), errorCode.getInfo());
    }

}
