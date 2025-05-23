package com.example.travel.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT(JSON Web Token) 관련 유틸리티 클래스
 * 
 * 이 클래스는 JWT 토큰의 생성, 검증, 파싱 등의 기능을 제공합니다.
 * Spring Security와 함께 사용되어 인증된 사용자의 정보를 안전하게 전달합니다.
 */
@Component
public class JwtUtils {
    /**
     * JWT 서명에 사용될 비밀키
     * application.properties에서 설정된 값을 주입받습니다.
     */
    private SecretKey key;

    /**
     * JWT 서명에 사용될 비밀키 문자열
     * application.properties에서 설정된 값을 주입받습니다.
     */
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    /**
     * JWT 토큰의 만료 시간 (밀리초 단위)
     * application.properties에서 설정된 값을 주입받습니다.
     */
    @Value("${app.jwt.expiration}")
    private long jwtExpiration;

    /**
     * Bean 초기화 시점에 비밀키를 생성합니다.
     * jwtSecret 문자열을 바이트 배열로 변환하여 HMAC-SHA 키를 생성합니다.
     */
    @PostConstruct
    public void init() {
        byte[] keyBytes = jwtSecret.getBytes();
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * JWT 토큰에서 사용자 이메일(주체)을 추출합니다.
     * 
     * @param token JWT 토큰
     * @return 토큰에 포함된 사용자 이메일
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * JWT 토큰에서 만료 시간을 추출합니다.
     * 
     * @param token JWT 토큰
     * @return 토큰의 만료 시간
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * JWT 토큰에서 특정 클레임을 추출합니다.
     * 
     * @param token JWT 토큰
     * @param claimsResolver 추출할 클레임을 지정하는 함수
     * @return 추출된 클레임 값
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * JWT 토큰의 모든 클레임을 추출합니다.
     * 
     * @param token JWT 토큰
     * @return 토큰의 모든 클레임
     */
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            return e.getClaims(); // 만료되었지만 claims 정보는 반환 가능
        }
    }

    /**
     * JWT 토큰의 만료 여부를 확인합니다.
     * 
     * @param token JWT 토큰
     * @return 토큰이 만료되었으면 true, 아니면 false
     */
    private Boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            return true;
        }
    }

    /**
     * 사용자 정보를 기반으로 JWT 토큰을 생성합니다.
     * 
     * @param userDetails 사용자 정보
     * @return 생성된 JWT 토큰
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * 클레임과 주체(사용자 이메일)를 기반으로 JWT 토큰을 생성합니다.
     * 
     * @param claims 토큰에 포함할 클레임
     * @param subject 토큰의 주체(사용자 이메일)
     * @return 생성된 JWT 토큰
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * JWT 토큰의 유효성을 검증합니다.
     * 토큰의 주체가 사용자 정보와 일치하고, 토큰이 만료되지 않았는지 확인합니다.
     * 
     * @param token JWT 토큰
     * @param userDetails 사용자 정보
     * @return 토큰이 유효하면 true, 아니면 false
     */
    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
} 