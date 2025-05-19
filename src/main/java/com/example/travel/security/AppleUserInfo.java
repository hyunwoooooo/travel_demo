package com.example.travel.security;

import com.example.travel.entity.User;

import java.util.Map;

/**
 * Apple OAuth2 인증으로부터 받은 사용자 정보를 처리하는 클래스
 */
public class AppleUserInfo extends OAuth2UserInfo {
    
    /**
     * 생성자
     * 
     * @param attributes Apple OAuth2로부터 받은 사용자 속성 정보
     */
    public AppleUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }
    
    /**
     * Apple에서 제공하는 사용자 고유 ID를 반환합니다.
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
     * @return 항상 "APPLE" 반환
     */
    @Override
    public String getProvider() {
        return User.Provider.APPLE.name();
    }
    
    /**
     * Apple 계정의 이메일을 반환합니다.
     * 
     * @return 사용자 이메일
     */
    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }
    
    /**
     * Apple 계정의 이름을 반환합니다.
     * 
     * @return 사용자 이름 (Apple은 일관된 이름 데이터를 제공하지 않을 수 있음)
     */
    @Override
    public String getName() {
        // Apple은 사용자 이름을 항상 제공하지 않으므로
        // 이메일에서 @ 이전 부분을 이름으로 사용하거나 null을 반환
        String email = getEmail();
        if (email != null && email.contains("@")) {
            return email.split("@")[0];
        }
        return null;
    }
} 