package com.example.travel.config;

import com.example.travel.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 설정 클래스
 * 
 * 애플리케이션의 보안 관련 설정을 정의합니다.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;
    
    /**
     * 생성자를 통한 의존성 주입
     * 
     * @param jwtAuthFilter JWT 인증 필터
     * @param userDetailsService 사용자 상세 정보 서비스
     */
    public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserDetailsService userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }
    
    /**
     * 비밀번호 인코더 빈을 생성합니다.
     * 
     * @return BCryptPasswordEncoder 인스턴스
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * 인증 제공자 빈을 생성합니다.
     * 
     * @return DaoAuthenticationProvider 인스턴스
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        
        return authProvider;
    }
    
    /**
     * 인증 관리자 빈을 생성합니다.
     * 
     * @param authConfig 인증 설정
     * @return AuthenticationManager 인스턴스
     * @throws Exception 예외 발생 시
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    /**
     * 보안 필터 체인을 구성합니다.
     * 
     * @param http HttpSecurity 객체
     * @return 구성된 SecurityFilterChain
     * @throws Exception 예외 발생 시
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> 
                auth.requestMatchers("/api/auth/**", "/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                    .anyRequest().authenticated()
            );
        
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
} 