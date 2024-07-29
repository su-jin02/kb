package com.unicorn.store.security;

import com.unicorn.store.model.User;
import com.unicorn.store.exceptions.http.CustomForbiddenException;
import com.unicorn.store.exceptions.token.ExpiredJwtTokenException;
import com.unicorn.store.exceptions.token.InvalidTokenException;
import com.unicorn.store.exceptions.ErrorCode;
import com.unicorn.store.security.dto.TokenResponseDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class TokenProvider {
    private static final String TOKEN_TYPE = "Bearer";

    private static final String AUTHORITY_KEY = "auth";

    private static final long ACCESS_TOKEN_EXPIRE_TIME_MILLIS = 24L * 60L * 60L * 1000L;

    private static final long REFRESH_TOKEN_EXPIRE_TIME_MILLIS = 30L * 24L * 60L * 60L * 1000L;

    private Key key;

    @Value("${SECURITY_JWT_KEY}")
    String secretKey;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    // 유저 정보를 가지고 AccessToken, RefreshToken 을 생성하는 메서드
    public TokenResponseDto generateTokenResponse(User user) {
        long now = new Date().getTime();
        String userinfo = user.getNickname();
        if (userinfo == null) {
            userinfo = user.getEmail(); //카카오로그인이라면 닉네임 대신 이메일 사용
        }
        return TokenResponseDto.builder()
                .tokenType(TOKEN_TYPE)
                .accessToken(generateToken(user))
                .expiresIn((now + ACCESS_TOKEN_EXPIRE_TIME_MILLIS) / 1000)
                .refreshToken(generateRefreshToken(user, new Date(now + REFRESH_TOKEN_EXPIRE_TIME_MILLIS)))
                .refreshTokenExpiresIn((now + REFRESH_TOKEN_EXPIRE_TIME_MILLIS) / 1000)
                .userId(user.getUserId())
                .nickname(userinfo)
                .build();
    }

    public String generateToken(User user) {
        long now = new Date().getTime();
        return Jwts.builder()
                .setSubject(user.getUserId().toString())
                .setExpiration(new Date(now + ACCESS_TOKEN_EXPIRE_TIME_MILLIS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateRefreshToken(User user, Date expDate) {
        return Jwts.builder()
                .setSubject(user.getUserId().toString())
                .setExpiration(expDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        if (ObjectUtils.isEmpty(claims.get(AUTHORITY_KEY))) {
            throw new CustomForbiddenException(ErrorCode.INVALID_TOKEN);
        }

        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(claims.get(AUTHORITY_KEY).toString()));
        return new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authorities);
    }

    public boolean isValidToken(String token) {
        parseClaims(token);
        return true;
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtTokenException(ErrorCode.EXPIRED_TOKEN);
        } catch (Exception e) {
            throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
        }
    }


    // Request Header에서 토큰 정보 추출
    public String getJwtToken(HttpServletRequest request) {
        String jwtToken = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .orElseThrow(NullPointerException::new);

        if (!StringUtils.hasText(jwtToken) || !jwtToken.startsWith(TOKEN_TYPE)) {
            throw new CustomForbiddenException(ErrorCode.INVALID_TOKEN);
        }

        return jwtToken.substring(7);
    }
}

