package com.example.travel.security;

import java.util.Map;

/**
 * OAuth2 소셜 로그인 제공자로부터 받은 사용자 정보를 추상화한 클래스
 * 
 * 다양한 소셜 로그인 제공자(Google, Naver, Kakao, Apple)의 사용자 정보를 일관된 방식으로 접근할 수 있게 합니다.
 */
public abstract class OAuth2UserInfo {
    
    /**
     * 소셜 로그인 제공자로부터 받은 원본 속성 정보
     */
    protected Map<String, Object> attributes;
    
    /**
     * 생성자
     * 
     * @param attributes 소셜 로그인 제공자로부터 받은 속성 정보
     */
    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
    
    /**
     * 속성 정보를 반환합니다.
     * 
     * @return 속성 정보 맵
     */
    public Map<String, Object> getAttributes() {
        return attributes;
    }
    
    /**
     * 소셜 로그인 제공자 ID를 반환합니다.
     * 
     * @return 소셜 로그인 제공자 ID
     */
    public abstract String getId();
    
    /**
     * 소셜 로그인 제공자 이름을 반환합니다.
     * 
     * @return 소셜 로그인 제공자 이름 (GOOGLE, NAVER, KAKAO, APPLE)
     */
    public abstract String getProvider();
    
    /**
     * 사용자 이메일을 반환합니다.
     * 
     * @return 사용자 이메일
     */
    public abstract String getEmail();
    
    /**
     * 사용자 이름을 반환합니다.
     * 
     * @return 사용자 이름
     */
    public abstract String getName();
} 