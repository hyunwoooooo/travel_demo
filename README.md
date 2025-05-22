# 🧳 Travel Product API

Spring Boot를 사용해 만든 **여행 상품 판매 백엔드 API** 예제입니다.  
기초 백엔드 개발자 교육용으로 설계되었으며, MySQL과 JPA를 활용한 CRUD 기능을 포함합니다.

---

## 🔧 기술 스택

- Java 17
- Spring Boot 3.x
- Spring Web
- Spring Data JPA (Hibernate)
- MySQL
- Swagger UI (API 문서)
- Postman (테스트용 컬렉션 제공)

---

## 📁 프로젝트 구조
com.example.travel
├── config
│   ├── OpenApiConfig.java         ← Swagger/OpenAPI 설정
│   └── SecurityConfig.java        ← Spring Security 설정
│
├── controller
│   ├── AuthController.java        ← 인증 관련 API 컨트롤러
│   └── UserController.java        ← 사용자 관련 API 컨트롤러
│
├── dto
│   ├── AuthRequest.java          ← 로그인 요청 DTO
│   ├── AuthResponse.java         ← 인증 응답 DTO
│   ├── SignUpRequest.java        ← 회원가입 요청 DTO
│   └── UpdateNameRequest.java    ← 이름 변경 요청 DTO
│
├── entity
│   └── User.java                 ← 사용자 엔티티
│
├── repository
│   └── UserRepository.java       ← 사용자 레포지토리
│
├── security
│   ├── JwtAuthFilter.java        ← JWT 인증 필터
│   └── JwtUtils.java            ← JWT 유틸리티
│
├── service
│   ├── AuthService.java          ← 인증 서비스
│   ├── UserService.java          ← 사용자 서비스
│   └── UserDetailsServiceImpl.java ← Spring Security 사용자 상세 서비스
│
└── TravelApplication.java        ← 메인 애플리케이션 클래스

주요 컴포넌트 설명
🔐 보안 관련
config/SecurityConfig.java: Spring Security 설정
security/JwtAuthFilter.java: JWT 토큰 검증 필터
security/JwtUtils.java: JWT 토큰 생성/검증 유틸리티
👤 사용자 관련
entity/User.java: 사용자 정보 엔티티
repository/UserRepository.java: 사용자 데이터 접근
service/UserService.java: 사용자 관련 비즈니스 로직
service/UserDetailsServiceImpl.java: Spring Security 사용자 인증 처리
🔑 인증 관련
controller/AuthController.java: 로그인/회원가입 API
service/AuthService.java: 인증 관련 비즈니스 로직
dto/AuthRequest.java, AuthResponse.java: 인증 관련 DTO
📝 DTO
dto/AuthRequest.java: 로그인 요청
dto/AuthResponse.java: 인증 응답
dto/SignUpRequest.java: 회원가입 요청
dto/UpdateNameRequest.java: 이름 변경 요청
📚 API 문서화
config/OpenApiConfig.java: Swagger/OpenAPI 설정
이 구조는 다음과 같은 특징을 가집니다:
계층형 아키텍처 (Controller-Service-Repository)
DTO를 통한 데이터 전송
JWT 기반 인증
Spring Security 통합
Swagger/OpenAPI 문서화