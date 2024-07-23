package com.fc.toyproeject2.global.security.filter;

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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {

        try {

        LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());

            return authenticationManager.authenticate(token);
        } catch (IOException e) {
            throw new AuthException(AuthErrorCode.LOGIN_FAILED);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws IOException {
        // 유저 아이디 가져오기
        CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();
        String email = userDetails.getEmail();

        // 유저 아이디로 access token 만들기
        Cookie accessToken = jwtUtil.getAccessTokenCookie(email);
        Cookie refreshToken = jwtUtil.getRefreshTokenCookie(email);

        // 응답에 쿠키 추가
        response.addCookie(accessToken);
        response.addCookie(refreshToken);

        response.setContentType("application/json; charset=utf-8");
        objectMapper.writeValue(response.getWriter(), APIUtil.OK());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, AuthenticationException failed)
        throws IOException {

        ErrorCode errorCode = AuthErrorCode.LOGIN_FAILED;

        response.setContentType("application/json; charset=utf-8");

        response.setStatus(errorCode.getCode().value());
        objectMapper.writeValue(response.getWriter(), APIUtil.ERROR(new AuthException(errorCode)));
    }

}
