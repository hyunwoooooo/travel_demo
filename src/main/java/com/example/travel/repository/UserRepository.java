package com.example.travel.repository;

import com.example.travel.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 사용자 엔티티에 대한 데이터 액세스를 제공하는 리포지토리 인터페이스
 * 
 * JpaRepository를 상속받아 기본적인 CRUD 연산 메소드를 자동으로 제공받습니다.
 * 추가로 필요한 쿼리 메소드를 정의할 수 있습니다.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * 이메일로 사용자 정보를 조회합니다.
     * 
     * @param email 조회할 사용자의 이메일
     * @return 사용자 정보를 담은 Optional 객체
     */
    Optional<User> findByEmail(String email);
    
    /**
     * 이메일이 존재하는지 확인합니다.
     * 
     * @param email 확인할 이메일
     * @return 이메일 존재 여부 (true: 존재함, false: 존재하지 않음)
     */
    boolean existsByEmail(String email);
    
    /**
     * 소셜 로그인 제공자와 제공자 ID로 사용자를 조회합니다.
     * 
     * @param provider 소셜 로그인 제공자 (GOOGLE, NAVER, KAKAO 등)
     * @param providerId 소셜 로그인 제공자에서 제공한 고유 ID
     * @return 사용자 정보를 담은 Optional 객체
     */
    Optional<User> findByProviderAndProviderId(User.Provider provider, String providerId);
} 