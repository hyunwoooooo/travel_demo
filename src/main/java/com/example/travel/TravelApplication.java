package com.example.travel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 여행 상품 애플리케이션의 진입점(Entry Point)
 * 
 * @SpringBootApplication 어노테이션은 다음 세 가지 어노테이션의 기능을 결합합니다:
 * 1. @Configuration: Spring의 자바 기반 설정 클래스임을 나타냅니다.
 * 2. @EnableAutoConfiguration: Spring Boot의 자동 설정을 활성화합니다.
 * 3. @ComponentScan: 현재 패키지와 하위 패키지에서 Spring 컴포넌트(@Component, @Service, @Repository, @Controller 등)를 찾아 빈으로 등록합니다.
 * 
 * 이 클래스는 애플리케이션을 시작하는 메인 메소드를 포함하고 있습니다.
 */
@SpringBootApplication
public class TravelApplication {

    /**
     * 애플리케이션 실행 메인 메소드
     * 
     * Spring Boot 애플리케이션을 시작하는 진입점입니다.
     * SpringApplication.run() 메소드는 다음과 같은 작업을 수행합니다:
     * 1. 애플리케이션 컨텍스트(ApplicationContext)를 생성합니다.
     * 2. 스프링 빈들을 등록하고 초기화합니다.
     * 3. 내장 웹 서버를 시작합니다.
     * 4. 커맨드 라인 인자를 처리합니다.
     * 
     * @param args 명령행 인자
     */
    public static void main(String[] args) {
        SpringApplication.run(TravelApplication.class, args);
    }

}
