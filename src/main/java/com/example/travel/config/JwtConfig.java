package com.example.travel.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

/**
 * JWT 설정 클래스
 * 
 * JWT 토큰 생성 및 검증에 필요한 설정 정보를 관리합니다.
 */
@Configuration
public class JwtConfig {
    
    /**
     * JWT 비밀키 (application.properties 또는 application.yml에서 설정)
     * 실제 운영 환경에서는 환경 변수 등으로 안전하게 관리해야 합니다.
     */
    @Value("${jwt.secret:defaultSecretKeyForDevelopmentEnvironmentOnly}")
    private String jwtSecret;
    
    /**
     * JWT 토큰 만료 시간 (초 단위, 기본값 24시간)
     */
    @Value("${jwt.expiration:86400}")
    private long jwtExpiration;
    
    /**
     * JWT 리프레시 토큰 만료 시간 (초 단위, 기본값 7일)
     */
    @Value("${jwt.refresh-expiration:604800}")
    private long refreshExpiration;
    
    /**
     * JWT 서명에 사용할 SecretKey 빈을 생성합니다.
     * 
     * @return JWT 서명용 SecretKey 객체
     */
    @Bean
    public SecretKey jwtSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
    
    /**
     * JWT 토큰 만료 시간을 반환합니다.
     * 
     * @return JWT 토큰 만료 시간(초)
     */
    public long getJwtExpiration() {
        return jwtExpiration;
    }
    
    /**
     * JWT 리프레시 토큰 만료 시간을 반환합니다.
     * 
     * @return JWT 리프레시 토큰 만료 시간(초)
     */
    public long getRefreshExpiration() {
        return refreshExpiration;
    }
} 