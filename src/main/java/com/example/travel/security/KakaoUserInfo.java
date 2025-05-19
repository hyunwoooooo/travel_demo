package com.example.travel.security;

import com.example.travel.entity.User;

import java.util.Map;

/**
 * Kakao OAuth2 인증으로부터 받은 사용자 정보를 처리하는 클래스
 */
public class KakaoUserInfo extends OAuth2UserInfo {
    
    /**
     * 생성자
     * 
     * @param attributes Kakao OAuth2로부터 받은 사용자 속성 정보
     */
    public KakaoUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }
    
    /**
     * Kakao에서 제공하는 사용자 고유 ID를 반환합니다.
     * 
     * @return 사용자 고유 ID
     */
    @Override
    public String getId() {
        return String.valueOf(attributes.get("id"));
    }
    
    /**
     * 제공자 이름을 반환합니다.
     * 
     * @return 항상 "KAKAO" 반환
     */
    @Override
    public String getProvider() {
        return User.Provider.KAKAO.name();
    }
    
    /**
     * Kakao 계정의 이메일을 반환합니다.
     * 
     * @return 사용자 이메일
     */
    @Override
    public String getEmail() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        if (kakaoAccount == null) {
            return null;
        }
        return (String) kakaoAccount.get("email");
    }
    
    /**
     * Kakao 계정의 이름을 반환합니다.
     * 
     * @return 사용자 이름
     */
    @Override
    public String getName() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        if (kakaoAccount == null) {
            return null;
        }
        
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        if (profile == null) {
            return null;
        }
        
        return (String) profile.get("nickname");
    }
} 