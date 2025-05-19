package com.example.travel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 소셜 로그인 요청 데이터를 담는 DTO 클래스
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "소셜 로그인 요청 DTO")
public class SocialLoginRequest {
    
    /**
     * 소셜 로그인 제공자로부터 받은 인증 코드 또는 토큰
     */
    @NotBlank(message = "액세스 토큰은 필수 입력 항목입니다.")
    @Schema(description = "액세스 토큰", example = "ya29.a0Ae4lvC0H...", required = true)
    private String accessToken;
    
    /**
     * 소셜 로그인 제공자 (GOOGLE, NAVER, KAKAO, APPLE)
     */
    @NotBlank(message = "소셜 로그인 제공자는 필수 입력 항목입니다.")
    @Schema(description = "소셜 로그인 제공자", example = "GOOGLE", required = true)
    private String provider;
} 