package com.fc.toyproeject2.global.util;

import com.fc.toyproeject2.domain.auth.model.userdetails.CustomUserDetails;
import com.fc.toyproeject2.global.exception.error.AuthErrorCode;
import com.fc.toyproeject2.global.exception.type.AuthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import javax.crypto.SecretKey;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Setter
    @Value("${spring.jwt.secret}")
    private String secretKeyStr;

    private Key secretKey;

    @Getter
    private final Long accessExpiredAt = 3600000L;

    @Getter
    private final Long refreshExpiredAt = 86400000L;


    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(secretKeyStr.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String email, Long expired) {
        return Jwts.builder()
                .signWith(secretKey)
                .expiration(new Date(System.currentTimeMillis() + expired))
                .issuedAt(new Date())
                .claim("email", email)
                .compact();
    }

    public String getEmailFromToken(String token) {
        return extractClaims(token).get("email", String.class);
    }

    public void isInvalidAccessToken(String token) {
        try {
            extractClaims(token);
        } catch (JwtException e) {
            throw new AuthException(AuthErrorCode.INVALID_TOKEN);
        }
    }

    public void isInvalidRefreshToken(String token) {
        try {
            extractClaims(token);
        } catch (ExpiredJwtException ex) {
            throw new AuthException(AuthErrorCode.EXPIRED_TOKEN);
        } catch (JwtException ex) {
            throw new AuthException(AuthErrorCode.INVALID_TOKEN);
        }
    }


    public Optional<String> getCookieValue(HttpServletRequest request, String cookieName) {
        if (request.getCookies() != null) {
            Optional<String> accseeToken = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals(cookieName))
                    .findFirst()
                    .map(Cookie::getValue);

            if (accseeToken.isEmpty())
                return Optional.empty();

            isInvalidAccessToken(accseeToken.get());
            return accseeToken;
        }
        return Optional.empty();
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Cookie getAccessTokenCookie(String email) {
        Cookie cookie = new Cookie("accessToken", generateToken(email, accessExpiredAt));
        cookie.setMaxAge((int) (accessExpiredAt / 1000));
        cookie.setPath("/");
        return cookie;
    }

    public Cookie getRefreshTokenCookie(String email) {
        Cookie cookie = new Cookie("refreshToken", generateToken(email, refreshExpiredAt));
        cookie.setMaxAge((int) (refreshExpiredAt / 1000));
        cookie.setPath("/");
        return cookie;
    }

    public CustomUserDetails getCustomUserDetailsFromToken(String accessTokenCookieValue) {
        String email = getEmailFromToken(accessTokenCookieValue);
        return new CustomUserDetails(
                null,
                email,
                null
        );
    }

    public String handleInvalidAccessToken(HttpServletResponse response, String refreshTokenCookie) {
        if (refreshTokenCookie == null) {
            throw new AuthException(AuthErrorCode.UNAUTHORIZED);
        }

        // refresh token 예외처리
        isInvalidRefreshToken(refreshTokenCookie);

        String email = getEmailFromToken(refreshTokenCookie);
        Cookie cookie = getAccessTokenCookie(email);

        response.addCookie(cookie);
        return cookie.getValue();
    }

    public boolean isEmpty(HttpServletRequest request, String tokenName) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null) return true;
        return Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(tokenName)).findFirst().isEmpty();
    }

}
