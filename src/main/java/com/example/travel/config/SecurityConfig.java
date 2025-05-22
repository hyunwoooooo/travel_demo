package com.example.travel.config;

import com.example.travel.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Spring Security 설정 클래스
 * 
 * 애플리케이션의 보안 관련 설정을 정의합니다.
 * JWT 기반의 인증 시스템을 구성하고, 보안 필터 체인을 설정합니다.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    /**
     * 사용자 정보를 로드하는 서비스
     */
    private final UserDetailsService userDetailsService;

    /**
     * JWT 인증 필터
     */
    private final JwtAuthFilter jwtAuthFilter;

    /**
     * 생성자를 통한 의존성 주입
     */
    public SecurityConfig(UserDetailsService userDetailsService, JwtAuthFilter jwtAuthFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthFilter = jwtAuthFilter;
    }
    
    /**
     * 비밀번호 인코더 빈 등록
     * BCrypt 알고리즘을 사용하여 비밀번호를 해시화합니다.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 인증 관리자 빈 등록
     * 사용자 인증을 처리하는 AuthenticationManager를 생성합니다.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 데이터베이스 인증 제공자 빈 등록
     * 사용자 정보를 데이터베이스에서 로드하여 인증을 처리합니다.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    
    /**
     * 보안 필터 체인 설정
     * 
     * 1. CSRF 보호 비활성화 (REST API이므로)
     * 2. 세션 사용하지 않음 (STATELESS)
     * 3. 인증이 필요한 경로 설정
     * 4. JWT 인증 필터 추가
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF 보호 비활성화 (REST API이므로)
            .csrf(AbstractHttpConfigurer::disable)
            // 세션 사용하지 않음 (STATELESS)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 인증이 필요한 경로 설정
            .authorizeHttpRequests(auth -> 
                auth.requestMatchers(
                    new AntPathRequestMatcher("/api/auth/**"),
                    new AntPathRequestMatcher("/v3/api-docs/**"),
                    new AntPathRequestMatcher("/swagger-ui/**"),
                    new AntPathRequestMatcher("/swagger-ui.html"),
                    new AntPathRequestMatcher("/swagger-resources/**"),
                    new AntPathRequestMatcher("/webjars/**")
                ).permitAll()
                .anyRequest().authenticated()
            )
            // 데이터베이스 인증 제공자 설정
            .authenticationProvider(authenticationProvider())
            // JWT 인증 필터 추가
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
} 