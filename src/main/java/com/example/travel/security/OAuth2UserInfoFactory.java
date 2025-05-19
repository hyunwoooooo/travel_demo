package com.example.travel.security;

import com.example.travel.entity.User;

import java.util.Map;

/**
 * OAuth2 제공자에 따라 적절한 사용자 정보 구현체를 생성하는 팩토리 클래스
 */
public class OAuth2UserInfoFactory {
    
    /**
     * 제공자 이름과 속성 정보를 기반으로 적절한 OAuth2UserInfo 구현체를 생성합니다.
     * 
     * @param providerName 소셜 로그인 제공자 이름 (GOOGLE, NAVER, KAKAO, APPLE)
     * @param attributes 소셜 로그인 제공자로부터 받은 속성 정보
     * @return 적절한 OAuth2UserInfo 구현체
     * @throws IllegalArgumentException 지원하지 않는 제공자인 경우 발생
     */
    public static OAuth2UserInfo getOAuth2UserInfo(String providerName, Map<String, Object> attributes) {
        User.Provider provider;
        
        try {
            provider = User.Provider.valueOf(providerName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("지원하지 않는 소셜 로그인 제공자입니다: " + providerName);
        }
        
        switch (provider) {
            case GOOGLE:
                return new GoogleUserInfo(attributes);
            case NAVER:
                return new NaverUserInfo(attributes);
            case KAKAO:
                return new KakaoUserInfo(attributes);
            case APPLE:
                return new AppleUserInfo(attributes);
            default:
                throw new IllegalArgumentException("지원하지 않는 소셜 로그인 제공자입니다: " + providerName);
        }
    }
} 