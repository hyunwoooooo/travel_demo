package com.example.travel.security;

import com.example.travel.config.JwtConfig;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT 토큰 생성 및 검증을 위한 유틸리티 클래스
 * 
 * JWT(JSON Web Token)를 사용하여 사용자 인증 정보를 안전하게 관리합니다.
 */
@Component
public class JwtUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    
    private final SecretKey jwtSecretKey;
    private final JwtConfig jwtConfig;
    
    /**
     * 생성자를 통한 의존성 주입
     * 
     * @param jwtSecretKey JWT 서명에 사용할 비밀키
     * @param jwtConfig JWT 설정 정보
     */
    public JwtUtils(SecretKey jwtSecretKey, JwtConfig jwtConfig) {
        this.jwtSecretKey = jwtSecretKey;
        this.jwtConfig = jwtConfig;
    }
    
    /**
     * JWT 토큰에서 사용자 이름(이메일)을 추출합니다.
     * 
     * @param token JWT 토큰
     * @return 사용자 이름(이메일)
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    /**
     * JWT 토큰에서 만료 시간을 추출합니다.
     * 
     * @param token JWT 토큰
     * @return 만료 시간
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    /**
     * JWT 토큰에서 특정 클레임을 추출합니다.
     * 
     * @param token JWT 토큰
     * @param claimsResolver 클레임 해석 함수
     * @return 추출된 클레임 값
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * JWT 토큰에서 모든 클레임을 추출합니다.
     * 
     * @param token JWT 토큰
     * @return 모든 클레임 정보
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    /**
     * JWT 토큰이 만료되었는지 확인합니다.
     * 
     * @param token JWT 토큰
     * @return 만료 여부 (true: 만료됨, false: 유효함)
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    /**
     * 사용자 인증 정보를 기반으로 JWT 액세스 토큰을 생성합니다.
     * 
     * @param authentication 사용자 인증 정보
     * @return 생성된 JWT 액세스 토큰
     */
    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return generateToken(userDetails.getUsername());
    }
    
    /**
     * 사용자 이메일을 기반으로 JWT 액세스 토큰을 생성합니다.
     * 
     * @param email 사용자 이메일
     * @return 생성된 JWT 액세스 토큰
     */
    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, email, jwtConfig.getJwtExpiration());
    }
    
    /**
     * 리프레시 토큰을 생성합니다.
     * 
     * @param email 사용자 이메일
     * @return 생성된 JWT 리프레시 토큰
     */
    public String generateRefreshToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, email, jwtConfig.getRefreshExpiration());
    }
    
    /**
     * JWT 토큰을 생성하는 내부 메소드입니다.
     * 
     * @param claims 토큰에 포함될 클레임 정보
     * @param subject 토큰의 주체 (사용자 이메일)
     * @param expiration 토큰 만료 시간(초)
     * @return 생성된 JWT 토큰
     */
    private String createToken(Map<String, Object> claims, String subject, long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(jwtSecretKey)
                .compact();
    }
    
    /**
     * JWT 토큰의 유효성을 검증합니다.
     * 
     * @param token 검증할 JWT 토큰
     * @param userDetails 사용자 세부 정보
     * @return 유효성 여부 (true: 유효함, false: 유효하지 않음)
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
    /**
     * JWT 토큰의 유효성을 검증합니다.
     * 
     * @param token 검증할 JWT 토큰
     * @return 유효성 여부 (true: 유효함, false: 유효하지 않음)
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(jwtSecretKey).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            logger.error("유효하지 않은 JWT 서명입니다: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("유효하지 않은 JWT 토큰입니다: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("만료된 JWT 토큰입니다: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("지원되지 않는 JWT 토큰입니다: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT 토큰이 비어있습니다: {}", e.getMessage());
        }
        return false;
    }
} 