package com.example.travel.service;

import com.example.travel.entity.User;
import com.example.travel.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring Security의 UserDetailsService 구현 클래스
 * 
 * 사용자 인증에 필요한 사용자 정보를 로드하는 역할을 합니다.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private final UserRepository userRepository;
    
    /**
     * 생성자를 통한 의존성 주입
     * 
     * @param userRepository 사용자 리포지토리
     */
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    /**
     * 주어진 사용자 이름(이메일)으로 사용자 정보를 로드합니다.
     * 
     * @param email 사용자 이메일
     * @return UserDetails 객체
     * @throws UsernameNotFoundException 사용자를 찾을 수 없을 때 발생
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("이메일로 사용자를 찾을 수 없습니다: " + email));
        
        return user;
    }
} 