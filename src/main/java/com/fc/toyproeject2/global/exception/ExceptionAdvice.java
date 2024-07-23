package com.fc.toyproeject2.global.exception;

import com.fc.toyproeject2.global.exception.type.*;
import com.fc.toyproeject2.global.util.APIUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(ItineraryException.class)
    public ResponseEntity itineraryException(ItineraryException ex) {
        return APIUtil.ERROR(ex);
    }

    @ExceptionHandler(TripCreateException.class)
    public ResponseEntity handleTripCreateException(TripCreateException ex) {
        return APIUtil.ERROR(ex);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity handleAuthException(AuthException ex) {
        return APIUtil.ERROR(ex);
    }

    @ExceptionHandler(TripReadException.class)
    public ResponseEntity tripReadException(TripReadException ex) {
        return APIUtil.ERROR(ex);
    }

    @ExceptionHandler(TripUpdateException.class)
    public ResponseEntity tripUpdateException(TripUpdateException ex) {
        return APIUtil.ERROR(ex);
    }

    @ExceptionHandler(DiscordException.class)
    public ResponseEntity discordException(DiscordException ex) {
        return APIUtil.ERROR(ex);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity validationException(ValidationException ex) {
        return APIUtil.ERROR(ex);
    }
    @ExceptionHandler(LikeException.class)
    public ResponseEntity likeException(LikeException ex) {
        return APIUtil.ERROR(ex);
    }

    @ExceptionHandler(CommentException.class)
    public ResponseEntity CommentException(CommentException ex) {return APIUtil.ERROR(ex);}

}
