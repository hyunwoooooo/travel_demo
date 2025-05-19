package com.example.travel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 인증 응답 데이터를 담는 DTO 클래스
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "인증 응답 DTO")
public class AuthResponse {
    
    /**
     * 사용자 이메일
     */
    @Schema(description = "이메일", example = "user@example.com")
    private String email;
    
    /**
     * 사용자 이름
     */
    @Schema(description = "이름", example = "홍길동")
    private String name;
    
    /**
     * JWT 액세스 토큰
     */
    @Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;
    
    /**
     * JWT 리프레시 토큰
     */
    @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String refreshToken;
    
    /**
     * 토큰 유형 (Bearer)
     */
    @Schema(description = "토큰 유형", example = "Bearer")
    private String tokenType;
    
    /**
     * 로그인 제공자 (LOCAL, GOOGLE, NAVER, KAKAO, APPLE)
     */
    @Schema(description = "로그인 제공자", example = "LOCAL")
    private String provider;
} 