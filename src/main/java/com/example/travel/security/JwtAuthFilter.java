package com.example.travel.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 인증 필터
 * 
 * 모든 HTTP 요청에 대해 JWT 토큰을 검증하고 인증 정보를 설정합니다.
 * Spring Security 필터 체인에 등록되어 요청이 처리되기 전에 실행됩니다.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);
    
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;
    
    /**
     * 생성자를 통한 의존성 주입
     * 
     * @param jwtUtils JWT 유틸리티
     * @param userDetailsService 사용자 상세 정보 서비스
     */
    public JwtAuthFilter(JwtUtils jwtUtils, UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }
    
    /**
     * 요청마다 JWT 토큰을 검증하고 인증 정보를 설정합니다.
     * 
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param filterChain 필터 체인
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateToken(jwt)) {
                String username = jwtUtils.extractUsername(jwt);
                
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("인증을 설정할 수 없습니다: {}", e.getMessage());
        }
        
        filterChain.doFilter(request, response);
    }
    
    /**
     * 요청 헤더에서 JWT 토큰을 추출합니다.
     * 
     * @param request HTTP 요청
     * @return JWT 토큰 문자열 또는 null
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        
        return null;
    }
} 