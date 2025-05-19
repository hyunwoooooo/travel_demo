package com.example.travel.security;

import com.example.travel.entity.User;

import java.util.Map;

/**
 * Naver OAuth2 인증으로부터 받은 사용자 정보를 처리하는 클래스
 */
public class NaverUserInfo extends OAuth2UserInfo {
    
    /**
     * 생성자
     * 
     * @param attributes Naver OAuth2로부터 받은 사용자 속성 정보
     */
    public NaverUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }
    
    /**
     * Naver에서 제공하는 사용자 고유 ID를 반환합니다.
     * 
     * @return 사용자 고유 ID
     */
    @Override
    public String getId() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        if (response == null) {
            return null;
        }
        return (String) response.get("id");
    }
    
    /**
     * 제공자 이름을 반환합니다.
     * 
     * @return 항상 "NAVER" 반환
     */
    @Override
    public String getProvider() {
        return User.Provider.NAVER.name();
    }
    
    /**
     * Naver 계정의 이메일을 반환합니다.
     * 
     * @return 사용자 이메일
     */
    @Override
    public String getEmail() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        if (response == null) {
            return null;
        }
        return (String) response.get("email");
    }
    
    /**
     * Naver 계정의 이름을 반환합니다.
     * 
     * @return 사용자 이름
     */
    @Override
    public String getName() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        if (response == null) {
            return null;
        }
        return (String) response.get("name");
    }
} 