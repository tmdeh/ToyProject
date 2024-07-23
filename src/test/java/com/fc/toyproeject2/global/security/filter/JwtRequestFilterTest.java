package com.fc.toyproeject2.global.security.filter;


import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fc.toyproeject2.global.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;


class JwtRequestFilterTest {
    private final String sampleEmail = "test@example.com";
    private JwtRequestFilter jwtRequestFilter;
    private JwtUtil jwtUtil;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @Mock
    private FilterChain filterChain;


    @BeforeEach
    public void setUp() {

        jwtUtil = new JwtUtil();
        ObjectMapper objectMapper = new ObjectMapper();

        jwtUtil.setSecretKeyStr("savemeyuseungdospringbootblogservicetest");
        jwtUtil.init();


        jwtRequestFilter = new JwtRequestFilter(jwtUtil, objectMapper, null);

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();

        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();

    }




    @Test
    @DisplayName("올바른 access token, refresh token인 경우")
    public void testSuccess() throws Exception {
        // given
        String accessToken = jwtUtil.generateToken(sampleEmail, jwtUtil.getAccessExpiredAt());
        String refreshToken = jwtUtil.generateToken(sampleEmail, jwtUtil.getRefreshExpiredAt());

        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);

        request.setCookies(accessTokenCookie);
        request.setCookies(refreshTokenCookie);

        // when
        jwtRequestFilter.doFilterInternal(request, response, filterChain);


        // then
        verify(filterChain, times(1)).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }


    @Test
    @DisplayName("access token 이 만료된 경우")
    public void testExpiredAccessToken() throws Exception {
        // given

        String accessToken = jwtUtil.generateToken(sampleEmail, -1L);
        String refreshToken = jwtUtil.generateToken(sampleEmail, jwtUtil.getRefreshExpiredAt());

        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);

        request.setCookies(accessTokenCookie);
        request.setCookies(refreshTokenCookie);

        // when
        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain, times(1)).doFilter(request, response);
        Cookie accessTokenCookieResult = response.getCookie("accessToken");
        assertNotNull(accessTokenCookieResult);
        // access token 이 재발급 되는지 확인
        assertTrue(accessTokenCookieResult.getMaxAge() > 0);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }


    @Test
    @DisplayName("refresh token 이 만료된 경우")
    public void testExpiredRefreshToken() throws Exception {
        // given
        String accessToken = jwtUtil.generateToken(sampleEmail, jwtUtil.getAccessExpiredAt());
        String refreshToken = jwtUtil.generateToken(sampleEmail, -1L);

        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);

        request.setCookies(accessTokenCookie);
        request.setCookies(refreshTokenCookie);

        // when
        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        // then
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    }


    @Test
    @DisplayName("access token 이 변조된 경우")
    public void testInvalidAccessToken() throws Exception {

        // given
        String accessToken = jwtUtil.generateToken(sampleEmail, jwtUtil.getAccessExpiredAt());
        String refreshToken = jwtUtil.generateToken(sampleEmail, -1L);

        accessToken += "failed";

        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);

        request.setCookies(accessTokenCookie);
        request.setCookies(refreshTokenCookie);

        // when
        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        // then
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    }


    @Test
    @DisplayName("refresh token 이 변조된 경우")
    public void testInvalidRefreshToken() throws Exception {
        // given
        String accessToken = jwtUtil.generateToken(sampleEmail, jwtUtil.getAccessExpiredAt());
        String refreshToken = jwtUtil.generateToken(sampleEmail, -1L);

        refreshToken += "failed";

        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);

        request.setCookies(accessTokenCookie);
        request.setCookies(refreshTokenCookie);

        // when
        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        // then
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    }
}