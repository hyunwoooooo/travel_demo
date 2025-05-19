package com.example.travel.controller;

import com.example.travel.dto.AuthRequest;
import com.example.travel.dto.AuthResponse;
import com.example.travel.dto.SocialLoginRequest;
import com.example.travel.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 인증 관련 API를 제공하는 컨트롤러 클래스
 * 
 * 로그인, 회원가입, 소셜 로그인, 토큰 갱신 등의 인증 관련 요청을 처리합니다.
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "인증 API", description = "로그인, 회원가입, 소셜 로그인, 토큰 갱신 등 인증 관련 API")
public class AuthController {
    
    private final AuthService authService;
    
    /**
     * 생성자를 통한 의존성 주입
     * 
     * @param authService 인증 서비스
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    /**
     * 이메일/비밀번호를 사용한 로그인 API
     * 
     * @param loginRequest 로그인 요청 정보
     * @return 인증 응답 정보
     */
    @PostMapping("/login")
    @Operation(
        summary = "일반 로그인", 
        description = "이메일과 비밀번호를 사용한 로그인을 처리하고 JWT 토큰을 발급합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "401", description = "잘못된 인증 정보", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    public ResponseEntity<AuthResponse> login(
            @Parameter(description = "로그인 요청 정보", required = true)
            @Valid @RequestBody AuthRequest loginRequest) {
        AuthResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 회원가입 API
     * 
     * @param signupRequest 회원가입 요청 정보
     * @return 인증 응답 정보
     */
    @PostMapping("/signup")
    @Operation(
        summary = "회원가입", 
        description = "새로운 사용자를 등록하고 자동으로 로그인 처리하여 JWT 토큰을 발급합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 (이메일 중복 등)", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    public ResponseEntity<AuthResponse> signup(
            @Parameter(description = "회원가입 요청 정보", required = true)
            @Valid @RequestBody AuthRequest signupRequest) {
        AuthResponse response = authService.signup(signupRequest);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 소셜 로그인 API
     * 
     * @param socialLoginRequest 소셜 로그인 요청 정보
     * @return 인증 응답 정보
     */
    @PostMapping("/social-login")
    @Operation(
        summary = "소셜 로그인", 
        description = "Google, Naver, Kakao, Apple 등의 소셜 계정으로 로그인하고 JWT 토큰을 발급합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "소셜 로그인 성공", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효하지 않은 소셜 토큰", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    public ResponseEntity<AuthResponse> socialLogin(
            @Parameter(description = "소셜 로그인 요청 정보 (제공자: GOOGLE, NAVER, KAKAO, APPLE)", required = true)
            @Valid @RequestBody SocialLoginRequest socialLoginRequest) {
        AuthResponse response = authService.socialLogin(socialLoginRequest);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 토큰 갱신 API
     * 
     * @param refreshToken 리프레시 토큰
     * @return 새로운, 인증 응답 정보
     */
    @PostMapping("/refresh")
    @Operation(
        summary = "토큰 갱신", 
        description = "리프레시 토큰을 사용하여 새로운 액세스 토큰과 리프레시 토큰을 발급받습니다."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "토큰 갱신 성공", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "400", description = "유효하지 않은 리프레시 토큰", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    public ResponseEntity<AuthResponse> refreshToken(
            @Parameter(description = "리프레시 토큰", required = true, example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
            @RequestParam String refreshToken) {
        AuthResponse response = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(response);
    }
} 