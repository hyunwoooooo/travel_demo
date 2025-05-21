package com.example.travel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "로그인 요청")
public class AuthRequest {
    
    @NotBlank(message = "이메일은 필수 입력값입니다")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    @Schema(description = "사용자 이메일", example = "test@example.com")
    private String email;
    
    @NotBlank(message = "비밀번호는 필수 입력값입니다")
    @Schema(description = "사용자 비밀번호", example = "password123")
    private String password;
} 