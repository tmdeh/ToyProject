package com.fc.toyproeject2.domain.register.service;

import com.fc.toyproeject2.domain.auth.model.entity.User;
import com.fc.toyproeject2.domain.auth.repository.UserRepository;
import com.fc.toyproeject2.domain.register.model.entity.EmailCheck;
import com.fc.toyproeject2.domain.register.model.request.EmailCheckRequest;
import com.fc.toyproeject2.domain.register.model.request.EmailSendRequest;
import com.fc.toyproeject2.domain.register.model.request.SingUpRequest;
import com.fc.toyproeject2.domain.register.repository.EmailCheckRepository;
import com.fc.toyproeject2.global.exception.error.AuthErrorCode;
import com.fc.toyproeject2.global.exception.type.AuthException;
import com.fc.toyproeject2.global.util.APIUtil;
import com.fc.toyproeject2.global.util.EmailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class RegisterService {
    private final UserRepository userRepository;
    private final EmailCheckRepository emailCheckRepository;
    private final EmailUtil emailUtil;
    private final PasswordEncoder pe;

    @Transactional
    public ResponseEntity singUp(SingUpRequest request) {

        EmailCheck emailCheck = emailCheckRepository.findByEmail(request.email())
                .orElseThrow(() -> {
                    throw new AuthException(AuthErrorCode.WRONG_EMAIL_SUCCESS_KEY);
                });

        if (emailCheck.getAccessKey().equals(request.successKey())) {
            userRepository.save(
                    new User(
                            null,
                            request.email(),
                            pe.encode(request.password()),
                            request.name(),
                            null,
                            null
                    )
            );

            emailCheckRepository.deleteByEmail(request.email());
            return APIUtil.OK();
        }

        throw new AuthException(AuthErrorCode.WRONG_EMAIL_SUCCESS_KEY);
    }

    @Transactional
    public ResponseEntity emailSend(EmailSendRequest request) {

        String successKey = getSuccessKey();

        try {
            emailUtil.sendSingUpRandomNumberEmail(request, successKey);
        } catch (Exception e) {
            throw new AuthException(AuthErrorCode.ERROR_SEND_EMAIL);
        }

        emailCheckRepository.findByEmail(request.email()).ifPresentOrElse(
                email -> {
                    email.setAccessKey(successKey);
                },
                () -> emailCheckRepository.save(EmailCheck.builder()
                        .accessKey(successKey)
                        .email(request.email())
                        .build())
        );

        return APIUtil.OK();
    }

    @Transactional
    public ResponseEntity emailCheck(EmailCheckRequest request) {
        EmailCheck emailCheck = emailCheckRepository.findByEmail(request.email())
                .orElseThrow(() -> {
                    throw new AuthException(AuthErrorCode.WRONG_EMAIL_SUCCESS_KEY);
                });

        if (emailCheck.getAccessKey().equals(request.successKey()))
            return APIUtil.OK();

        throw new AuthException(AuthErrorCode.WRONG_EMAIL_SUCCESS_KEY);
    }

    private String getSuccessKey() {
        String randomStr = "";
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            randomStr += random.nextInt(9);
        }
        return randomStr;
    }

}
