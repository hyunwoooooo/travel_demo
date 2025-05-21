package com.example.travel.controller;

import com.example.travel.dto.AuthRequest;
import com.example.travel.dto.AuthResponse;
import com.example.travel.dto.SignUpRequest;
import com.example.travel.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "인증", description = "인증 관련 API")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(
        summary = "로그인",
        description = "이메일과 비밀번호로 로그인하여 JWT 토큰을 발급받습니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content)
    })
    public ResponseEntity<AuthResponse> login(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "로그인 요청 (이메일, 비밀번호)",
            required = true,
            content = @Content(schema = @Schema(implementation = AuthRequest.class))
        )
        @RequestBody @Valid AuthRequest request
    ) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/signup")
    @Operation(
        summary = "회원가입",
        description = "새로운 사용자를 등록하고 JWT 토큰을 발급받습니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
    })
    public ResponseEntity<AuthResponse> signup(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "회원가입 요청 (이메일, 비밀번호, 이름)",
            required = true,
            content = @Content(schema = @Schema(implementation = SignUpRequest.class))
        )
        @RequestBody @Valid SignUpRequest request
    ) {
        return ResponseEntity.ok(authService.signup(request));
    }
} 