package com.example.travel.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger 설정 클래스
 * 
 * API 문서화를 위한 Swagger(OpenAPI) 관련 설정을 정의합니다.
 * @Configuration 어노테이션은 이 클래스가 Spring의 설정 클래스임을 나타냅니다.
 * 
 * Swagger를 사용하면:
 * 1. REST API를 자동으로 문서화할 수 있습니다.
 * 2. API를 시각적으로 탐색하고 테스트할 수 있는 UI를 제공합니다.
 * 3. API 스펙을 표준화된 방식으로 관리할 수 있습니다.
 * 
 * springdoc-openapi는 Spring Boot 애플리케이션의 OpenAPI 3 스펙을 자동으로 생성합니다.
 */
@Configuration
public class SwaggerConfig {

    /**
     * OpenAPI 구성을 정의하는 빈
     * 
     * @Bean 어노테이션은 이 메소드가 Spring IoC 컨테이너에 의해 관리되는 빈을 생성함을 나타냅니다.
     * 
     * 이 메소드는 API 문서의 메타데이터(제목, 버전, 설명 등)를 설정합니다.
     * 
     * 접근 경로:
     * - Swagger UI: /swagger-ui.html
     * - OpenAPI 문서: /api-docs
     * 
     * @return OpenAPI 설정 객체
     */
    @Bean
    public OpenAPI openAPI() {
        // API 정보 설정
        Info info = new Info()
                .title("여행 상품 API")  // API 문서 제목
                .version("v1.0")  // API 버전
                .description("여행 상품 관리를 위한 REST API 문서입니다.")  // API 설명
                .license(new License().name("Apache 2.0").url("http://springdoc.org"));  // 라이센스 정보

        // OpenAPI 객체 생성 및 설정
        return new OpenAPI()
                .info(info)  // API 정보 설정
                .servers(List.of(new Server().url("/")));  // API 서버 URL 설정
    }
} 