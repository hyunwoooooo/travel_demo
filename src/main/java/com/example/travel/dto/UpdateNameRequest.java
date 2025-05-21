package com.example.travel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "이름 변경 요청")
public class UpdateNameRequest {
    
    @NotBlank(message = "이름은 필수 입력값입니다")
    @Schema(description = "새로운 이름", example = "홍길동")
    private String name;
} 