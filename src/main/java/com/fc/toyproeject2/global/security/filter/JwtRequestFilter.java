package com.fc.toyproeject2.global.security.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fc.toyproeject2.domain.auth.model.userdetails.CustomUserDetails;
import com.fc.toyproeject2.global.exception.error.AuthErrorCode;
import com.fc.toyproeject2.global.exception.type.AuthException;
import com.fc.toyproeject2.global.util.APIUtil;
import com.fc.toyproeject2.global.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final Map<String, String> excludeUrls;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        String method = request.getMethod();
        return excludeUrls.entrySet().stream().anyMatch(entry -> path.equals(entry.getKey()) && method.equalsIgnoreCase(entry.getValue()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {

        try {
            if (request.getRequestURI().equals("/api/trip")
                    && jwtUtil.isEmpty(request, "accessToken") && jwtUtil.isEmpty(request, "refreshToken")) {
                filterChain.doFilter(request, response);
                return;
            }

            String refreshTokenCookieValue = jwtUtil.getCookieValue(request, "refreshToken")
                    .orElseThrow(() -> new AuthException(AuthErrorCode.EXPIRED_TOKEN));


            String accessTokenCookieValue = jwtUtil.getCookieValue(request, "accessToken")
                    .orElseGet(() -> jwtUtil.handleInvalidAccessToken(response, refreshTokenCookieValue));


            CustomUserDetails customUserDetails = jwtUtil.getCustomUserDetailsFromToken(accessTokenCookieValue);

            Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails,
                    null,
                    customUserDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (AuthException ex) {
            handleException(response, ex);
        } catch (JwtException ex) {
            handleException(response, new AuthException(AuthErrorCode.INVALID_TOKEN));
        }

    }

    private void handleException(HttpServletResponse response, AuthException ex) throws IOException {
        response.setStatus(ex.getStatusCode().value());
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(APIUtil.ERROR(ex)));
    }

}

