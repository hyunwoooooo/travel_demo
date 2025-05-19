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
} 