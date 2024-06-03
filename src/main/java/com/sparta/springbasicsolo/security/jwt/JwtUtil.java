package com.sparta.springbasicsolo.security.jwt;

import com.sparta.springbasicsolo.domain.user.repository.entity.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {
    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String REFRESH_TOKEN_KEY = "refresh_token";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료시간
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분
//    private final long TOKEN_TIME = 5 * 1000L; // 5초

    private final long REFRESH_TOKEN_TIME = 7 * 24 * 60 * 60 * 1000L; // 7일
//    private final long REFRESH_TOKEN_TIME = 10 * 1000L; // 10초

    private Key key;

    @Value("${jwt.secret.key}")
    String secretKey;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createJwtToken(String username, UserRoleEnum role) {
        return BEARER_PREFIX +
                Jwts.builder()
                        .claim("category", "access")
                        .claim("username", username)
                        .claim(AUTHORIZATION_KEY, role)
                        .setIssuedAt(new Date(System.currentTimeMillis()))
                        .setExpiration(new Date(System.currentTimeMillis() + TOKEN_TIME))
                        .signWith(key, SignatureAlgorithm.HS256)
                        .compact();
    }

    public String createRefreshToken(String username, UserRoleEnum role) {
        return BEARER_PREFIX +
                Jwts.builder()
                        .claim("category", "refresh")
                        .claim("username", username)
                        .claim(AUTHORIZATION_KEY, role)
                        .setIssuedAt(new Date(System.currentTimeMillis()))
                        .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_TIME))
                        .signWith(key, SignatureAlgorithm.HS256)
                        .compact();
    }

    public String newAccessToken(String refreshToken) {
        Claims claims = getBodyFromToken(refreshToken);

        if (!"refresh".equals(claims.get("category", String.class))) {
            throw new JwtException("리프레시 토큰이 없거나 아님");
        }

        String username = claims.get("username", String.class);

        return createJwtToken(username, UserRoleEnum.USER);
    }

    public void addJwtToCookie(String key, String token, HttpServletResponse res) {
        // Cookie Value 에는 공백이 불가능해서 encoding 진행
        token = URLEncoder.encode(token, StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        Cookie cookie = new Cookie(key, token);
        cookie.setPath("/");

        res.addCookie(cookie);
    }

    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(BEARER_PREFIX.length());
        }
        log.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    public Claims getBodyFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String getUsername(String token) {
        return getBodyFromToken(token).get("username", String.class);
    }

    public String getCategory(String token) {
        return getBodyFromToken(token).get("category", String.class);
    }

    public String getTokenFromRequest(String key, HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) {
                    String decodeToken = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                    return substringToken(decodeToken);
                }
            }
        }
        return null;
    }
}