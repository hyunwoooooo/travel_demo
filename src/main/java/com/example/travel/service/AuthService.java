package com.example.travel.service;

import com.example.travel.dto.AuthRequest;
import com.example.travel.dto.AuthResponse;
import com.example.travel.dto.SignUpRequest;
import com.example.travel.entity.User;
import com.example.travel.repository.UserRepository;
import com.example.travel.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 인증 관련 서비스 클래스
 * 
 * 로그인, 회원가입 등 인증 관련 비즈니스 로직을 처리합니다.
 * Spring Security와 JWT를 사용하여 인증을 구현합니다.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    /**
     * Spring Security의 인증 관리자
     * 사용자 인증을 처리합니다.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * 사용자 정보 저장소
     */
    private final UserRepository userRepository;

    /**
     * 비밀번호 인코더
     * 비밀번호를 안전하게 해시화합니다.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * JWT 토큰 유틸리티
     * JWT 토큰의 생성과 검증을 담당합니다.
     */
    private final JwtUtils jwtUtils;

    /**
     * 사용자 로그인 처리
     * 
     * 1. 사용자 인증
     * 2. JWT 토큰 생성
     * 3. 사용자 정보와 토큰을 포함한 응답 반환
     * 
     * @param request 로그인 요청 정보 (이메일, 비밀번호)
     * @return 인증 응답 (JWT 토큰, 사용자 정보)
     */
    public AuthResponse login(AuthRequest request) {
        // 사용자 인증
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // SecurityContext에 인증 정보 설정
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // JWT 토큰 생성
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtUtils.generateToken(userDetails);
        
        // 사용자 정보 조회
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 인증 응답 반환
        return new AuthResponse(jwt, user.getEmail(), user.getName(), user.getRole().name());
    }

    /**
     * 사용자 회원가입 처리
     * 
     * 1. 이메일 중복 체크
     * 2. 새 사용자 생성
     * 3. 자동 로그인 처리
     * 4. JWT 토큰 생성
     * 
     * @param request 회원가입 요청 정보 (이메일, 비밀번호, 이름)
     * @return 인증 응답 (JWT 토큰, 사용자 정보)
     */
    @Transactional
    public AuthResponse signup(SignUpRequest request) {
        // 이메일 중복 체크
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("이미 사용 중인 이메일입니다.");
        }

        // 새 사용자 생성
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setRole(User.UserRole.USER);

        // 사용자 정보 저장
        userRepository.save(user);

        // 자동 로그인 처리
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // SecurityContext에 인증 정보 설정
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // JWT 토큰 생성
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtUtils.generateToken(userDetails);

        // 인증 응답 반환
        return new AuthResponse(jwt, user.getEmail(), user.getName(), user.getRole().name());
    }
} 