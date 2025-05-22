package com.example.travel.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * 사용자 엔티티 클래스
 * 
 * 사용자 정보를 저장하는 JPA 엔티티입니다.
 * Spring Security의 UserDetails 인터페이스를 구현하여 인증에 사용됩니다.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
public class User implements UserDetails {
    
    /**
     * 사용자 ID
     * 자동 생성되는 기본 키
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 사용자 이메일
     * 로그인에 사용되는 고유 식별자
     */
    @Column(unique = true, nullable = false)
    private String email;
    
    /**
     * 사용자 비밀번호
     * 암호화되어 저장됨
     */
    @Column(nullable = false)
    private String password;
    
    /**
     * 사용자 이름
     */
    @Column(nullable = false)
    private String name;
    
    /**
     * 사용자 역할
     * USER 또는 ADMIN
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.USER;
    
    /**
     * 사용자 역할 열거형
     */
    public enum UserRole {
        USER, ADMIN
    }

    /**
     * 사용자의 권한 목록 반환
     * Spring Security에서 사용자의 권한을 확인하는데 사용됩니다.
     * 
     * @return 사용자의 권한 목록
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    /**
     * 사용자 이름 반환
     * Spring Security에서 사용자를 식별하는데 사용됩니다.
     * 이메일을 사용자 이름으로 사용합니다.
     * 
     * @return 사용자 이메일
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * 계정 만료 여부
     * 
     * @return 계정이 만료되지 않았으면 true
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정 잠금 여부
     * 
     * @return 계정이 잠기지 않았으면 true
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 비밀번호 만료 여부
     * 
     * @return 비밀번호가 만료되지 않았으면 true
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 계정 활성화 여부
     * 
     * @return 계정이 활성화되어 있으면 true
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
} 