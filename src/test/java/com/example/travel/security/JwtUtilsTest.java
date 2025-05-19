package com.example.travel.security;

import com.example.travel.config.JwtConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

/**
 * JwtUtils 단위 테스트 클래스
 */
@SpringBootTest
public class JwtUtilsTest {

    private static final String SECRET_KEY = "thisIsATestKeyForJwtUtilsTestingPurposesOnlyAndItMustBeLongEnough";
    private static final long EXPIRATION_TIME = 3600; // 1시간
    private static final long REFRESH_EXPIRATION_TIME = 86400; // 24시간

    private JwtUtils jwtUtils;
    private SecretKey secretKey;

    @Mock
    private JwtConfig jwtConfig;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        openMocks(this);
        secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        
        when(jwtConfig.getJwtExpiration()).thenReturn(EXPIRATION_TIME);
        when(jwtConfig.getRefreshExpiration()).thenReturn(REFRESH_EXPIRATION_TIME);
        
        jwtUtils = new JwtUtils(secretKey, jwtConfig);
    }

    @Test
    @DisplayName("토큰에서 사용자 이름을 추출할 수 있어야 함")
    void shouldExtractUsername() {
        // Given
        String email = "test@example.com";
        String token = createToken(email);

        // When
        String extractedEmail = jwtUtils.extractUsername(token);

        // Then
        assertThat(extractedEmail).isEqualTo(email);
    }

    @Test
    @DisplayName("이메일로부터 토큰을 생성할 수 있어야 함")
    void shouldGenerateTokenFromEmail() {
        // Given
        String email = "test@example.com";

        // When
        String token = jwtUtils.generateToken(email);

        // Then
        assertThat(token).isNotNull();
        String extractedEmail = jwtUtils.extractUsername(token);
        assertThat(extractedEmail).isEqualTo(email);
    }

    @Test
    @DisplayName("이메일로부터 리프레시 토큰을 생성할 수 있어야 함")
    void shouldGenerateRefreshToken() {
        // Given
        String email = "test@example.com";

        // When
        String refreshToken = jwtUtils.generateRefreshToken(email);

        // Then
        assertThat(refreshToken).isNotNull();
        String extractedEmail = jwtUtils.extractUsername(refreshToken);
        assertThat(extractedEmail).isEqualTo(email);
    }

    /**
     * 테스트용 토큰 생성 헬퍼 메소드
     */
    private String createToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1시간
                .signWith(secretKey)
                .compact();
    }
} 