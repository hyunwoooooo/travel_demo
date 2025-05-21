package com.example.travel;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {
    
    @Test
    public void generatePasswordHash() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // 테스트 계정 1
        String password1 = "test1234";
        String encodedPassword1 = encoder.encode(password1);
        System.out.println("Test Account 1:");
        System.out.println("Email: test1@example.com");
        System.out.println("Password: " + password1);
        System.out.println("Hash: " + encodedPassword1);
        System.out.println("----------------------------------------");
        
        // 테스트 계정 2
        String password2 = "test5678";
        String encodedPassword2 = encoder.encode(password2);
        System.out.println("Test Account 2:");
        System.out.println("Email: test2@example.com");
        System.out.println("Password: " + password2);
        System.out.println("Hash: " + encodedPassword2);
        System.out.println("----------------------------------------");
        
        // 검증
        System.out.println("Verification:");
        System.out.println("Account 1 matches: " + encoder.matches(password1, encodedPassword1));
        System.out.println("Account 2 matches: " + encoder.matches(password2, encodedPassword2));
    }
} 