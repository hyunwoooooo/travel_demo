package com.example.travel.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 인증 필터
 * 
 * 모든 HTTP 요청에 대해 JWT 토큰을 검증하고 인증을 처리하는 필터입니다.
 * Spring Security의 필터 체인에 등록되어 요청마다 실행됩니다.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    /**
     * JWT 토큰 관련 유틸리티
     */
    private final JwtUtils jwtUtils;

    /**
     * 사용자 정보를 로드하는 서비스
     */
    private final UserDetailsService userDetailsService;

    /**
     * HTTP 요청을 처리하고 JWT 토큰을 검증합니다.
     * 
     * 1. Authorization 헤더에서 JWT 토큰을 추출
     * 2. 토큰이 유효하면 사용자 정보를 로드
     * 3. SecurityContext에 인증 정보를 설정
     * 
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param filterChain 필터 체인
     * @throws ServletException 서블릿 예외
     * @throws IOException IO 예외
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        // Authorization 헤더에서 JWT 토큰 추출
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Authorization 헤더가 없거나 Bearer 토큰이 아닌 경우 다음 필터로 진행
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Bearer 접두어를 제거하고 토큰 추출
        jwt = authHeader.substring(7).trim();
        userEmail = jwtUtils.extractUsername(jwt);

        // 토큰이 유효하고 현재 인증된 사용자가 없는 경우
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 사용자 정보 로드
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            
            // 토큰이 유효한 경우 인증 정보 설정
            if (jwtUtils.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
} 