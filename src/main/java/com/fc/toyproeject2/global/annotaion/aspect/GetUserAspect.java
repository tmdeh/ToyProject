package com.fc.toyproeject2.global.annotaion.aspect;

import com.fc.toyproeject2.domain.auth.model.userdetails.CustomUserDetails;
import com.fc.toyproeject2.domain.auth.repository.UserRepository;
import com.fc.toyproeject2.global.annotaion.GetUser;
import com.fc.toyproeject2.global.exception.error.AuthErrorCode;
import com.fc.toyproeject2.global.exception.type.AuthException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class GetUserAspect {

    private final UserRepository userRepository;

    @Around("@annotation(getUser)")
    public Object aroundMethodWithCustomParameter(ProceedingJoinPoint joinPoint, GetUser getUser) throws Throwable {

        Object[] args = joinPoint.getArgs();

        boolean check = true;

        for (Object arg : args) {
            if (arg instanceof CustomUserDetails) {
                CustomUserDetails customUserDetails = (CustomUserDetails) arg;
                String email = customUserDetails.getEmail();
                customUserDetails.setUser(userRepository.findByEmail(email).orElseThrow(() -> new AuthException(AuthErrorCode.NOT_FOUND)));
                check = false;
            }
        }

        if (check)
            throw new AuthException(AuthErrorCode.NOT_FOUND);

        return joinPoint.proceed(args);
    }
}
