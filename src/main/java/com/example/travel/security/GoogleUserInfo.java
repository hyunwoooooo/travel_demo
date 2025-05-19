package com.example.travel.security;

import com.example.travel.entity.User;

import java.util.Map;

/**
 * Google OAuth2 인증으로부터 받은 사용자 정보를 처리하는 클래스
 */
public class GoogleUserInfo extends OAuth2UserInfo {
    
    /**
     * 생성자
     * 
     * @param attributes Google OAuth2로부터 받은 사용자 속성 정보
     */
    public GoogleUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }
    
    /**
     * Google에서 제공하는 사용자 고유 ID를 반환합니다.
     * 
     * @return 사용자 고유 ID
     */
    @Override
    public String getId() {
        return (String) attributes.get("sub");
    }
    
    /**
     * 제공자 이름을 반환합니다.
     * 
     * @return 항상 "GOOGLE" 반환
     */
    @Override
    public String getProvider() {
        return User.Provider.GOOGLE.name();
    }
    
    /**
     * Google 계정의 이메일을 반환합니다.
     * 
     * @return 사용자 이메일
     */
    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }
    
    /**
     * Google 계정의 이름을 반환합니다.
     * 
     * @return 사용자 이름
     */
    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
} 