package com.fc.toyproeject2.global.annotaion.aspect;

import com.fc.toyproeject2.domain.auth.model.userdetails.CustomUserDetails;
import com.fc.toyproeject2.domain.auth.repository.UserRepository;
import com.fc.toyproeject2.domain.itinerary.model.entity.Move;
import com.fc.toyproeject2.domain.itinerary.repository.MoveRepository;
import com.fc.toyproeject2.global.annotaion.ValidationMoveInUser;
import com.fc.toyproeject2.global.exception.error.ValidationErrorCode;
import com.fc.toyproeject2.global.exception.type.ValidationException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ValidationMoveInUserAspect {

    private final UserRepository userRepository;
    private final MoveRepository moveRepository;

    @Around("@annotation(validationMoveInUser)")
    public Object aroundMethodWithCustomParameter(ProceedingJoinPoint joinPoint, ValidationMoveInUser validationMoveInUser) throws Throwable {

        Object[] args = joinPoint.getArgs();
        Long user = null;
        Long objectId = null;

        for (Object arg : args) {
            if (arg instanceof CustomUserDetails) {
                CustomUserDetails customUserDetails = (CustomUserDetails) arg;
                String email = customUserDetails.getEmail();
                user = userRepository.findByEmail(email).orElseThrow(() -> new ValidationException(ValidationErrorCode.NOT_FOUND_USER)).getId();
            } else if (arg instanceof Long) {
                Move obj = moveRepository.findById((Long) arg).orElseThrow(
                        () -> new ValidationException(ValidationErrorCode.NOT_FOUND_OBJECT)
                );
                objectId = obj.getTrip().getUser().getId();
            }
        }

        if (objectId == null)
            throw new ValidationException(ValidationErrorCode.NOT_FOUND_OBJECT);

        if (user == null)
            throw new ValidationException(ValidationErrorCode.NOT_FOUND_USER);

        if (user != objectId)
            throw new ValidationException(ValidationErrorCode.NOT_MASTER_USER);

        return joinPoint.proceed(args);
    }

}
