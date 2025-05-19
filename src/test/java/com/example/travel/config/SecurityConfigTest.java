package com.example.travel.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Spring Security 설정 테스트 클래스
 */
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("인증되지 않은 사용자는 보호된 엔드포인트에 접근할 수 없어야 함")
    void unauthenticatedUserCannotAccessProtectedEndpoint() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/travel-products"))
                .andReturn();
        
        int status = result.getResponse().getStatus();
        // 401(Unauthorized) 또는 403(Forbidden)이 나올 수 있음
        assert(status == 401 || status == 403);
    }

    @Test
    @DisplayName("인증되지 않은 사용자도 인증 관련 엔드포인트에 접근할 수 있어야 함")
    void unauthenticatedUserCanAccessAuthEndpoints() throws Exception {
        // 404는 페이지가 없다는 것이지 접근이 거부된 것이 아님
        // 404 상태 코드도 엔드포인트에 접근할 수 있음을 의미함
        mockMvc.perform(get("/api/auth/any-endpoint"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("인증되지 않은 사용자도 Swagger 문서에 접근할 수 있어야 함")
    void unauthenticatedUserCanAccessSwaggerDocs() throws Exception {
        // Swagger UI는 정적 리소스로, 404 또는 200이 반환될 수 있음
        MvcResult result = mockMvc.perform(get("/swagger-ui/index.html"))
                .andReturn();
        
        int status = result.getResponse().getStatus();
        // 200(OK) 또는 404(Not Found)가 나올 수 있음 (정적 리소스 접근이므로)
        assert(status == 200 || status == 404);
    }

    @Test
    @WithMockUser
    @DisplayName("인증된 사용자는 보호된 엔드포인트에 접근할 수 있어야 함")
    void authenticatedUserCanAccessProtectedEndpoint() throws Exception {
        // 컨트롤러가 없으면 404를 반환하지만 인증은 통과한 것
        MvcResult result = mockMvc.perform(get("/api/travel-products"))
                .andReturn();
        
        MockHttpServletResponse response = result.getResponse();
        int status = response.getStatus();
        
        // 404(Not Found)는 리소스가 없다는 것이지 인증 문제가 아님
        // 401이나 403이 아니면 인증은 통과한 것
        assert(status != 401 && status != 403);
    }
} 