package com.example.travel.controller;

import com.example.travel.dto.UpdateNameRequest;
import com.example.travel.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "사용자", description = "사용자 관련 API")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @PostMapping("/name")
    @Operation(
        summary = "이름 변경",
        description = "현재 로그인한 사용자의 이름을 변경합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "이름 변경 성공"),
        @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
    })
    public ResponseEntity<Void> updateName(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody @Valid UpdateNameRequest request
    ) {
        userService.updateName(userDetails.getUsername(), request.getName());
        return ResponseEntity.ok().build();
    }
} 