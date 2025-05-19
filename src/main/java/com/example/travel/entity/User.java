package com.example.travel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * 사용자 엔티티 클래스
 * 
 * JPA를 사용하여 'users' 테이블과 매핑됩니다.
 * Spring Security의 UserDetails 인터페이스를 구현하여 인증 및 권한 관리에 활용됩니다.
 */
@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    /**
     * 사용자의 고유 식별자(PK)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 사용자 이메일 (로그인 ID로 사용)
     * 유일한 값으로 설정하고 null이 될 수 없음
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * 사용자 비밀번호
     * 기본 로그인 시 사용하며, 보안을 위해 암호화하여 저장
     */
    private String password;

    /**
     * 사용자 이름
     */
    private String name;

    /**
     * 소셜 로그인 제공자 (GOOGLE, NAVER, KAKAO, APPLE, LOCAL)
     */
    @Enumerated(EnumType.STRING)
    private Provider provider;

    /**
     * 소셜 로그인 제공자로부터 받은 고유 ID
     */
    private String providerId;

    /**
     * 사용자 역할 (ROLE_USER, ROLE_ADMIN)
     */
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * 소셜 로그인 제공자 열거형
     */
    public enum Provider {
        LOCAL, GOOGLE, NAVER, KAKAO, APPLE
    }

    /**
     * 사용자 역할 열거형
     */
    public enum Role {
        ROLE_USER, ROLE_ADMIN
    }

    /**
     * 사용자의 권한 정보를 반환합니다.
     * Spring Security에서 사용자의 권한을 확인할 때 호출됩니다.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    /**
     * 사용자의 아이디를 반환합니다. (이메일을 사용)
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * 계정이 만료되지 않았는지 반환합니다.
     * true: 만료되지 않음, false: 만료됨
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정이 잠겨있지 않은지 반환합니다.
     * true: 잠겨있지 않음, false: 잠김
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 자격 증명(비밀번호)이 만료되지 않았는지 반환합니다.
     * true: 만료되지 않음, false: 만료됨
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 계정이 활성화(사용 가능)인지 반환합니다.
     * true: 활성화, false: 비활성화
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
} 