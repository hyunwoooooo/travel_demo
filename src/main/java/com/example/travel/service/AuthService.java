package com.example.travel.service;

import com.example.travel.dto.AuthRequest;
import com.example.travel.dto.AuthResponse;
import com.example.travel.dto.SocialLoginRequest;
import com.example.travel.entity.User;
import com.example.travel.repository.UserRepository;
import com.example.travel.security.JwtUtils;
import com.example.travel.security.OAuth2UserInfo;
import com.example.travel.security.OAuth2UserInfoFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 사용자 인증 및 회원가입 관련 비즈니스 로직을 처리하는 서비스 클래스
 */
@Service
public class AuthService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RestTemplate restTemplate;
    
    /**
     * 생성자를 통한 의존성 주입
     * 
     * @param userRepository 사용자 리포지토리
     * @param passwordEncoder 비밀번호 인코더
     * @param authenticationManager 인증 관리자
     * @param jwtUtils JWT 유틸리티
     */
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.restTemplate = new RestTemplate();
    }
    
    /**
     * 사용자 로그인을 처리합니다.
     * 
     * @param loginRequest 로그인 요청 정보
     * @return 인증 응답 정보
     */
    public AuthResponse login(AuthRequest loginRequest) {
        // 인증 요청
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        
        // SecurityContext에 인증 정보 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // 사용자 정보 조회
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        // JWT 토큰 생성
        String accessToken = jwtUtils.generateToken(user.getEmail());
        String refreshToken = jwtUtils.generateRefreshToken(user.getEmail());
        
        // 응답 생성
        return AuthResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .provider(user.getProvider().name())
                .build();
    }
    
    /**
     * 사용자 회원가입을 처리합니다.
     * 
     * @param signupRequest 회원가입 요청 정보
     * @return 인증 응답 정보
     */
    public AuthResponse signup(AuthRequest signupRequest) {
        // 이메일 중복 검사
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new RuntimeException("이미 사용 중인 이메일입니다.");
        }
        
        // 사용자 생성 및 저장
        User user = User.builder()
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .name(signupRequest.getName())
                .provider(User.Provider.LOCAL)
                .role(User.Role.ROLE_USER)
                .build();
        
        userRepository.save(user);
        
        // 인증 요청
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signupRequest.getEmail(), signupRequest.getPassword()));
        
        // SecurityContext에 인증 정보 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // JWT 토큰 생성
        String accessToken = jwtUtils.generateToken(authentication);
        String refreshToken = jwtUtils.generateRefreshToken(user.getEmail());
        
        // 응답 생성
        return AuthResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .provider(user.getProvider().name())
                .build();
    }
    
    /**
     * 소셜 로그인을 처리합니다.
     * 
     * @param socialLoginRequest 소셜 로그인 요청 정보
     * @return 인증 응답 정보
     */
    public AuthResponse socialLogin(SocialLoginRequest socialLoginRequest) {
        try {
            // 소셜 로그인 제공자의 사용자 정보 API 호출
            Map<String, Object> userAttributes = getUserAttributesFromProvider(
                    socialLoginRequest.getProvider(), socialLoginRequest.getAccessToken());
            
            // 사용자 정보 추출
            OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                    socialLoginRequest.getProvider(), userAttributes);
            
            // 사용자 조회 또는 생성
            User user = processUserRegistration(userInfo);
            
            // JWT 토큰 생성
            String accessToken = jwtUtils.generateToken(user.getEmail());
            String refreshToken = jwtUtils.generateRefreshToken(user.getEmail());
            
            // 응답 생성
            return AuthResponse.builder()
                    .email(user.getEmail())
                    .name(user.getName())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .provider(user.getProvider().name())
                    .build();
            
        } catch (Exception e) {
            logger.error("소셜 로그인 처리 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("소셜 로그인 처리 중 오류가 발생했습니다.", e);
        }
    }
    
    /**
     * 소셜 로그인 제공자로부터 사용자 정보를 가져옵니다.
     * 
     * @param provider 소셜 로그인 제공자
     * @param accessToken 액세스 토큰
     * @return 사용자 속성 정보
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> getUserAttributesFromProvider(String provider, String accessToken) {
        String userInfoEndpoint = getUserInfoEndpoint(provider);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                userInfoEndpoint, HttpMethod.GET, entity, Map.class);
        
        return response.getBody();
    }
    
    /**
     * 소셜 로그인 제공자의 사용자 정보 API 엔드포인트를 반환합니다.
     * 
     * @param provider 소셜 로그인 제공자
     * @return 사용자 정보 API 엔드포인트
     */
    private String getUserInfoEndpoint(String provider) {
        switch (provider.toUpperCase()) {
            case "GOOGLE":
                return "https://www.googleapis.com/oauth2/v3/userinfo";
            case "NAVER":
                return "https://openapi.naver.com/v1/nid/me";
            case "KAKAO":
                return "https://kapi.kakao.com/v2/user/me";
            case "APPLE":
                return "https://appleid.apple.com/auth/userinfo"; // 실제 엔드포인트와 다를 수 있음
            default:
                throw new IllegalArgumentException("지원하지 않는 소셜 로그인 제공자입니다: " + provider);
        }
    }
    
    /**
     * 소셜 로그인 사용자의 등록을 처리합니다.
     * 기존 사용자가 있으면 조회하고, 없으면 새로 생성합니다.
     * 
     * @param userInfo 소셜 로그인 사용자 정보
     * @return 사용자 엔티티
     */
    private User processUserRegistration(OAuth2UserInfo userInfo) {
        // 소셜 로그인 제공자 ID로 사용자 조회
        User.Provider provider = User.Provider.valueOf(userInfo.getProvider());
        
        return userRepository.findByProviderAndProviderId(provider, userInfo.getId())
                .orElseGet(() -> {
                    // 이메일로 기존 사용자 조회
                    return userRepository.findByEmail(userInfo.getEmail())
                            .map(existingUser -> {
                                // 기존 사용자가 있으면 소셜 로그인 정보 업데이트
                                // (이 부분은 필요에 따라 구현할 수 있음)
                                return existingUser;
                            })
                            .orElseGet(() -> {
                                // 새 사용자 생성
                                User newUser = User.builder()
                                        .email(userInfo.getEmail())
                                        .name(userInfo.getName())
                                        .provider(provider)
                                        .providerId(userInfo.getId())
                                        .role(User.Role.ROLE_USER)
                                        .build();
                                
                                return userRepository.save(newUser);
                            });
                });
    }
    
    /**
     * 리프레시 토큰을 사용하여 새 액세스 토큰을 생성합니다.
     * 
     * @param refreshToken 리프레시 토큰
     * @return 새 액세스 토큰과 리프레시 토큰을 포함한 인증 응답
     */
    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtUtils.validateToken(refreshToken)) {
            throw new RuntimeException("유효하지 않은 리프레시 토큰입니다.");
        }
        
        String email = jwtUtils.extractUsername(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        String newAccessToken = jwtUtils.generateToken(email);
        String newRefreshToken = jwtUtils.generateRefreshToken(email);
        
        return AuthResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .provider(user.getProvider().name())
                .build();
    }
} 