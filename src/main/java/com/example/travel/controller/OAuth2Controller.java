package com.example.travel.controller;

import com.example.travel.entity.User;
import com.example.travel.repository.UserRepository;
import com.example.travel.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth/oauth2")
@RequiredArgsConstructor
public class OAuth2Controller {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @GetMapping("/success")
    public String oauth2Success(@RequestParam String token, Model model) {
        // 토큰에서 사용자 이메일 추출
        String email = jwtUtils.extractUsername(token);
        // 사용자 정보 조회 (필요시)
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        // 토큰을 모델에 담아 전달
        model.addAttribute("token", token);
        return "success";
    }
} 