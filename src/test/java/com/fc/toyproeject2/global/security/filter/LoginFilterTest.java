package com.fc.toyproeject2.global.security.filter;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fc.toyproeject2.domain.auth.model.request.LoginRequest;
import com.fc.toyproeject2.domain.auth.model.userdetails.CustomUserDetails;
import com.fc.toyproeject2.global.exception.error.AuthErrorCode;
import com.fc.toyproeject2.global.exception.error.ErrorCode;
import com.fc.toyproeject2.global.exception.type.AuthException;
import com.fc.toyproeject2.global.util.APIUtil;
import com.fc.toyproeject2.global.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import java.io.InputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.mockito.Mockito.*;

public class LoginFilterTest {

    @InjectMocks
    private LoginFilter loginFilter;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    private MockHttpServletResponse response;
    private MockHttpServletRequest request;


    @Test
    @DisplayName("로그인 검증 성공")
    public void testAttemptAuthentication() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest("test@example.com", "samplepassword");
        when(objectMapper.readValue(request.getInputStream(), LoginRequest.class)).thenReturn(loginRequest);


        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
            loginRequest.email(), loginRequest.password());

        Authentication authResult = mock(Authentication.class);
        when(authenticationManager.authenticate(authRequest)).thenReturn(authResult);


        // when
        Authentication result = loginFilter.attemptAuthentication(request, response);

        // then
        assertNotNull(result);
        assertEquals(result, authResult);
    }
    
    


    @Test
    @DisplayName("토큰 발급 후 쿠키에 저장")
    public void testSuccessfulAuthentication() throws Exception {
        // given
        FilterChain chain = mock(FilterChain.class);

        String accessTokenSample = "access-token-value";
        String refreshTokenSample = "refresh-token-value";
        String email = "test@example.com";

        Cookie accessTokenCookie = new Cookie("accessToken", accessTokenSample);
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshTokenSample);


        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getEmail()).thenReturn(email);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(jwtUtil.getAccessTokenCookie(email)).thenReturn(accessTokenCookie);
        when(jwtUtil.getRefreshTokenCookie(email)).thenReturn(refreshTokenCookie);


        // when
        loginFilter.successfulAuthentication(request, response, chain, authentication);


        // then 토큰이 제대로 저장이 되어 있는지 확인
        assertEquals("application/json; charset=utf-8", response.getContentType());
        Cookie accessCookie = response.getCookie("accessToken");
        Cookie refreshCookie = response.getCookie("refreshToken");

        assertNotNull(accessCookie);
        assertNotNull(refreshCookie);

        String accessToken = accessCookie.getValue();
        String refreshToken = refreshCookie.getValue();

        assertEquals(accessToken, accessTokenSample);
        assertEquals(refreshToken, refreshTokenSample);

        verify(objectMapper).writeValue(response.getWriter(), APIUtil.OK());
    }


    @Test
    @DisplayName("로그인 실패 처리")
    public void testUnsuccessfulAuthentication() throws Exception {
        // given
        AuthenticationException failed = mock(AuthenticationException.class);
        ErrorCode errorCode = AuthErrorCode.LOGIN_FAILED;

        // when
        loginFilter.unsuccessfulAuthentication(request, response, failed);

        // then
        assertEquals("application/json; charset=utf-8", response.getContentType());
        assertEquals(errorCode.getCode().value(), response.getStatus());
        verify(objectMapper).writeValue(response.getWriter(), APIUtil.ERROR(new AuthException(errorCode)));
    }

    @Test
    @DisplayName("로그인 실패 예외")
    public void testFailedAuthentication() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest("test@example.com", "invalidpassword");
        when(objectMapper.readValue(any(InputStream.class), eq(LoginRequest.class))).thenReturn(
            loginRequest);

        UsernamePasswordAuthenticationToken authRequest =
            new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());

        when(authenticationManager.authenticate(authRequest)).thenThrow(new AuthException(AuthErrorCode.LOGIN_FAILED));

        // when & then
        assertThrows(AuthException.class,
            () -> loginFilter.attemptAuthentication(request, response));
    }

}
