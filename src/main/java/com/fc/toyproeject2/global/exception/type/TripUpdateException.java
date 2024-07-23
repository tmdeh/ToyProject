package com.fc.toyproeject2.global.exception.type;

import com.fc.toyproeject2.global.exception.error.TripUpdateErrorCode;
import org.springframework.web.client.HttpStatusCodeException;

public class TripUpdateException extends HttpStatusCodeException {
    public TripUpdateException(TripUpdateErrorCode errorCode) {
        super(errorCode.getCode(), errorCode.getInfo());
    }
}
