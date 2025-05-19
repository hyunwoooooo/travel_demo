package com.example.travel.controller;

import com.example.travel.dto.AuthRequest;
import com.example.travel.dto.AuthResponse;
import com.example.travel.dto.SocialLoginRequest;
import com.example.travel.entity.User;
import com.example.travel.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * AuthController 통합 테스트 클래스
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("로그인 API 테스트")
    void loginTest() throws Exception {
        // Given
        AuthRequest loginRequest = new AuthRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        AuthResponse mockResponse = AuthResponse.builder()
                .email("test@example.com")
                .name("테스트유저")
                .accessToken("test-access-token")
                .refreshToken("test-refresh-token")
                .tokenType("Bearer")
                .provider(User.Provider.LOCAL.name())
                .build();

        when(authService.login(any(AuthRequest.class))).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.name").value("테스트유저"))
                .andExpect(jsonPath("$.accessToken").value("test-access-token"))
                .andExpect(jsonPath("$.refreshToken").value("test-refresh-token"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.provider").value("LOCAL"));
    }

    @Test
    @DisplayName("회원가입 API 테스트")
    void signupTest() throws Exception {
        // Given
        AuthRequest signupRequest = new AuthRequest();
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setPassword("password123");
        signupRequest.setName("신규유저");

        AuthResponse mockResponse = AuthResponse.builder()
                .email("newuser@example.com")
                .name("신규유저")
                .accessToken("test-access-token")
                .refreshToken("test-refresh-token")
                .tokenType("Bearer")
                .provider(User.Provider.LOCAL.name())
                .build();

        when(authService.signup(any(AuthRequest.class))).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("newuser@example.com"))
                .andExpect(jsonPath("$.name").value("신규유저"))
                .andExpect(jsonPath("$.accessToken").value("test-access-token"))
                .andExpect(jsonPath("$.refreshToken").value("test-refresh-token"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.provider").value("LOCAL"));
    }

    @Test
    @DisplayName("소셜 로그인 API 테스트")
    void socialLoginTest() throws Exception {
        // Given
        SocialLoginRequest socialLoginRequest = new SocialLoginRequest();
        socialLoginRequest.setAccessToken("social-access-token");
        socialLoginRequest.setProvider("GOOGLE");

        AuthResponse mockResponse = AuthResponse.builder()
                .email("google_user@example.com")
                .name("구글유저")
                .accessToken("test-access-token")
                .refreshToken("test-refresh-token")
                .tokenType("Bearer")
                .provider(User.Provider.GOOGLE.name())
                .build();

        when(authService.socialLogin(any(SocialLoginRequest.class))).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(post("/api/auth/social-login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(socialLoginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("google_user@example.com"))
                .andExpect(jsonPath("$.name").value("구글유저"))
                .andExpect(jsonPath("$.accessToken").value("test-access-token"))
                .andExpect(jsonPath("$.refreshToken").value("test-refresh-token"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.provider").value("GOOGLE"));
    }

    @Test
    @DisplayName("토큰 갱신 API 테스트")
    void refreshTokenTest() throws Exception {
        // Given
        String refreshToken = "valid-refresh-token";

        AuthResponse mockResponse = AuthResponse.builder()
                .email("test@example.com")
                .name("테스트유저")
                .accessToken("new-access-token")
                .refreshToken("new-refresh-token")
                .tokenType("Bearer")
                .provider(User.Provider.LOCAL.name())
                .build();

        when(authService.refreshToken(refreshToken)).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(post("/api/auth/refresh")
                        .param("refreshToken", refreshToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.name").value("테스트유저"))
                .andExpect(jsonPath("$.accessToken").value("new-access-token"))
                .andExpect(jsonPath("$.refreshToken").value("new-refresh-token"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.provider").value("LOCAL"));
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 존재하지 않는 사용자")
    void loginFailTest_UserNotFound() throws Exception {
        when(authService.login(any(AuthRequest.class)))
            .thenThrow(new AuthenticationException("존재하지 않는 사용자입니다."));

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new AuthRequest("notfound@example.com", "wrongpass"))))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value("존재하지 않는 사용자입니다."));
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 잘못된 비밀번호")
    void loginFailTest_WrongPassword() throws Exception {
        when(authService.login(any(AuthRequest.class)))
            .thenThrow(new AuthenticationException("비밀번호가 일치하지 않습니다."));

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new AuthRequest("user@example.com", "wrongpass"))))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value("비밀번호가 일치하지 않습니다."));
    }

    @Test
    @DisplayName("회원가입 실패 테스트 - 이미 존재하는 이메일")
    void signupFailTest_DuplicateEmail() throws Exception {
        when(authService.signup(any(AuthRequest.class)))
            .thenThrow(new RuntimeException("이미 존재하는 이메일입니다."));

        AuthRequest signupRequest = new AuthRequest();
        signupRequest.setEmail("existing@example.com");
        signupRequest.setPassword("password123");
        signupRequest.setName("기존유저");

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("이미 존재하는 이메일입니다."));
    }

    @Test
    @DisplayName("토큰 갱신 실패 테스트 - 유효하지 않은 리프레시 토큰")
    void refreshTokenFailTest_InvalidToken() throws Exception {
        when(authService.refreshToken(anyString()))
            .thenThrow(new RuntimeException("유효하지 않은 리프레시 토큰입니다."));

        mockMvc.perform(post("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"refreshToken\": \"invalid-token\"}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("유효하지 않은 리프레시 토큰입니다."));
    }

    @Test
    @DisplayName("소셜 로그인 실패 테스트 - 지원하지 않는 제공자")
    void socialLoginFailTest_UnsupportedProvider() throws Exception {
        when(authService.socialLogin(anyString(), anyString()))
            .thenThrow(new RuntimeException("지원하지 않는 소셜 로그인 제공자입니다."));

        mockMvc.perform(post("/api/auth/social")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"provider\": \"UNKNOWN\", \"token\": \"social-token\"}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("지원하지 않는 소셜 로그인 제공자입니다."));
    }

    @Test
    @DisplayName("로그아웃 실패 테스트 - 유효하지 않은 토큰")
    void logoutFailTest_InvalidToken() throws Exception {
        doThrow(new RuntimeException("유효하지 않은 토큰입니다."))
            .when(authService).logout(anyString());

        mockMvc.perform(post("/api/auth/logout")
                .header("Authorization", "Bearer invalid-token"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("유효하지 않은 토큰입니다."));
    }
} 