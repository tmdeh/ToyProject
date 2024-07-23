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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private EmailCheckRepository emailCheckRepository;
    @Mock
    private EmailUtil emailUtil;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegisterService registerService;

    private final String testEmail = "test@example.com";
    private final String testPassword = "password";
    private final String testName = "testName";
    private final String successKey = "123456";

    @Test
    void testSingUpSuccess() {
        SingUpRequest request = new SingUpRequest(testEmail, testPassword, successKey, testName, successKey);
        EmailCheck emailCheck = new EmailCheck(null, testEmail, successKey);

        when(emailCheckRepository.findByEmail(eq(testEmail))).thenReturn(Optional.of(emailCheck));
        when(passwordEncoder.encode(eq(testPassword))).thenReturn("encodedPassword");

        ResponseEntity response = registerService.singUp(request);

        assertEquals(APIUtil.OK(), response);
        verify(userRepository, times(1)).save(any(User.class));
        verify(emailCheckRepository, times(1)).deleteByEmail(eq(testEmail));
    }

    @Test
    void testSingUpFailureWrongKey() {
        SingUpRequest request = new SingUpRequest(testEmail, testPassword, "wrong", testName, successKey);
        EmailCheck emailCheck = new EmailCheck(null, testEmail, successKey);

        when(emailCheckRepository.findByEmail(eq(testEmail))).thenReturn(Optional.of(emailCheck));

        assertThrows(AuthException.class, () -> registerService.singUp(request));
        verify(userRepository, times(0)).save(any(User.class));
        verify(emailCheckRepository, times(0)).deleteByEmail(eq(testEmail));
    }

    @Test
    void testEmailSendSuccess() {
        EmailSendRequest request = new EmailSendRequest(testEmail);

        doNothing().when(emailUtil).sendSingUpRandomNumberEmail(eq(request), anyString());

        ResponseEntity response = registerService.emailSend(request);

        assertEquals(APIUtil.OK(), response);
        verify(emailCheckRepository, times(1)).save(any(EmailCheck.class));
    }

    @Test
    void testEmailSendFailure() {
        EmailSendRequest request = new EmailSendRequest(testEmail);

        doThrow(new AuthException(AuthErrorCode.ERROR_SEND_EMAIL)).when(emailUtil).sendSingUpRandomNumberEmail(eq(request), anyString());

        assertThrows(AuthException.class, () -> registerService.emailSend(request));
        verify(emailCheckRepository, times(0)).save(any(EmailCheck.class));
    }

    @Test
    void testEmailCheckSuccess() {
        EmailCheckRequest request = new EmailCheckRequest(testEmail, successKey);
        EmailCheck emailCheck = new EmailCheck(null, testEmail, successKey);

        when(emailCheckRepository.findByEmail(eq(testEmail))).thenReturn(Optional.of(emailCheck));

        ResponseEntity response = registerService.emailCheck(request);

        assertEquals(APIUtil.OK(), response);
    }

    @Test
    void testEmailCheckFailure() {
        EmailCheckRequest request = new EmailCheckRequest(testEmail, "wrongKey");
        EmailCheck emailCheck = new EmailCheck(null, testEmail, successKey);


        when(emailCheckRepository.findByEmail(eq(testEmail))).thenReturn(Optional.of(emailCheck));

        assertThrows(RuntimeException.class, () -> registerService.emailCheck(request));
    }
}