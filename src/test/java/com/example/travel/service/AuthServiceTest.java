package com.example.travel.service;

import com.example.travel.dto.AuthRequest;
import com.example.travel.dto.AuthResponse;
import com.example.travel.entity.User;
import com.example.travel.repository.UserRepository;
import com.example.travel.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * AuthService 단위 테스트 클래스
 */
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private AuthRequest loginRequest;
    private AuthRequest signupRequest;

    @BeforeEach
    void setUp() {
        // 테스트용 사용자 설정
        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("encodedPassword")
                .name("테스트유저")
                .provider(User.Provider.LOCAL)
                .role(User.Role.ROLE_USER)
                .build();

        // 로그인 요청 정보 설정
        loginRequest = new AuthRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        // 회원가입 요청 정보 설정
        signupRequest = new AuthRequest();
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setPassword("password123");
        signupRequest.setName("신규유저");
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void loginSuccessTest() {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(jwtUtils.generateToken(anyString())).thenReturn("test-access-token");
        when(jwtUtils.generateRefreshToken(anyString())).thenReturn("test-refresh-token");

        // When
        AuthResponse response = authService.login(loginRequest);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(response.getName()).isEqualTo(testUser.getName());
        assertThat(response.getAccessToken()).isEqualTo("test-access-token");
        assertThat(response.getRefreshToken()).isEqualTo("test-refresh-token");
        assertThat(response.getProvider()).isEqualTo(User.Provider.LOCAL.name());

        // Verify
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail(loginRequest.getEmail());
        verify(jwtUtils).generateToken(testUser.getEmail());
        verify(jwtUtils).generateRefreshToken(testUser.getEmail());
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    void signupSuccessTest() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(
                User.builder()
                        .id(2L)
                        .email(signupRequest.getEmail())
                        .password("encodedPassword")
                        .name(signupRequest.getName())
                        .provider(User.Provider.LOCAL)
                        .role(User.Role.ROLE_USER)
                        .build()
        );
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtils.generateToken(any(Authentication.class))).thenReturn("test-access-token");
        when(jwtUtils.generateRefreshToken(anyString())).thenReturn("test-refresh-token");

        // When
        AuthResponse response = authService.signup(signupRequest);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo(signupRequest.getEmail());
        assertThat(response.getName()).isEqualTo(signupRequest.getName());
        assertThat(response.getAccessToken()).isEqualTo("test-access-token");
        assertThat(response.getRefreshToken()).isEqualTo("test-refresh-token");
        assertThat(response.getProvider()).isEqualTo(User.Provider.LOCAL.name());

        // Verify
        verify(userRepository).existsByEmail(signupRequest.getEmail());
        verify(passwordEncoder).encode(signupRequest.getPassword());
        verify(userRepository).save(any(User.class));
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils).generateToken(any(Authentication.class));
        verify(jwtUtils).generateRefreshToken(anyString());
    }

    @Test
    @DisplayName("회원가입 실패 테스트 - 이메일 중복")
    void signupFailTest_duplicateEmail() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.signup(signupRequest);
        });

        assertThat(exception.getMessage()).contains("이미 사용 중인 이메일입니다");

        // Verify
        verify(userRepository).existsByEmail(signupRequest.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("토큰 갱신 성공 테스트")
    void refreshTokenSuccessTest() {
        // Given
        String refreshToken = "valid-refresh-token";
        when(jwtUtils.validateToken(refreshToken)).thenReturn(true);
        when(jwtUtils.extractUsername(refreshToken)).thenReturn(testUser.getEmail());
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(jwtUtils.generateToken(testUser.getEmail())).thenReturn("new-access-token");
        when(jwtUtils.generateRefreshToken(testUser.getEmail())).thenReturn("new-refresh-token");

        // When
        AuthResponse response = authService.refreshToken(refreshToken);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(response.getName()).isEqualTo(testUser.getName());
        assertThat(response.getAccessToken()).isEqualTo("new-access-token");
        assertThat(response.getRefreshToken()).isEqualTo("new-refresh-token");

        // Verify
        verify(jwtUtils).validateToken(refreshToken);
        verify(jwtUtils).extractUsername(refreshToken);
        verify(userRepository).findByEmail(testUser.getEmail());
        verify(jwtUtils).generateToken(testUser.getEmail());
        verify(jwtUtils).generateRefreshToken(testUser.getEmail());
    }

    @Test
    @DisplayName("토큰 갱신 실패 테스트 - 유효하지 않은 토큰")
    void refreshTokenFailTest_invalidToken() {
        // Given
        String refreshToken = "invalid-refresh-token";
        when(jwtUtils.validateToken(refreshToken)).thenReturn(false);

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.refreshToken(refreshToken);
        });

        assertThat(exception.getMessage()).contains("유효하지 않은 리프레시 토큰입니다");

        // Verify
        verify(jwtUtils).validateToken(refreshToken);
        verify(jwtUtils, never()).extractUsername(anyString());
        verify(userRepository, never()).findByEmail(anyString());
    }
} 