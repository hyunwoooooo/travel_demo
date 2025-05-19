package com.example.travel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 로그인 및 회원가입 요청 데이터를 담는 DTO 클래스
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "인증 요청 DTO")
public class AuthRequest {
    
    /**
     * 사용자 이메일
     */
    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "유효한 이메일 주소 형식이 아닙니다.")
    @Schema(description = "이메일", example = "user@example.com", required = true)
    private String email;
    
    /**
     * 사용자 비밀번호
     */
    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다.")
    @Schema(description = "비밀번호", example = "password123", required = true)
    private String password;
    
    /**
     * 사용자 이름 (회원가입시에만 필요)
     */
    @Schema(description = "이름", example = "홍길동")
    private String name;
} 