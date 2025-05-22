# 🧳 Travel Product API

Spring Boot를 사용해 만든 **여행 상품 판매 백엔드 API 예제**입니다.  
기초 백엔드 개발자 교육용으로 설계되었으며, MySQL과 JPA를 활용한 CRUD 기능을 포함합니다.

---

## 🔧 기술 스택

- Java 17
- Spring Boot 3.x
- Spring Web
- Spring Data JPA (Hibernate)
- MySQL
- Swagger UI (API 문서화)
- Postman (테스트용 컬렉션 제공)

---

## 📁 프로젝트 구조 (설명 중심)

- **`config`**
    - `OpenApiConfig.java`: Swagger/OpenAPI 설정
    - `SecurityConfig.java`: Spring Security 설정

- **`controller`**
    - `AuthController.java`: 로그인/회원가입 등 인증 관련 API
    - `UserController.java`: 사용자 정보 조회 및 수정 API

- **`dto`**
    - `AuthRequest.java`: 로그인 요청 데이터
    - `AuthResponse.java`: 로그인 성공 응답 데이터
    - `SignUpRequest.java`: 회원가입 요청 데이터
    - `UpdateNameRequest.java`: 사용자 이름 수정 요청

- **`entity`**
    - `User.java`: 사용자 정보를 저장하는 JPA 엔티티

- **`repository`**
    - `UserRepository.java`: 사용자 데이터베이스 접근 레포지토리

- **`security`**
    - `JwtAuthFilter.java`: HTTP 요청에 포함된 JWT 검증 필터
    - `JwtUtils.java`: JWT 생성 및 검증 도구 클래스

- **`service`**
    - `AuthService.java`: 로그인/회원가입 등의 인증 비즈니스 로직
    - `UserService.java`: 사용자 조회 및 수정 로직
    - `UserDetailsServiceImpl.java`: Spring Security 인증용 사용자 정보 제공

- **`TravelApplication.java`**
    - Spring Boot 메인 클래스 (앱 실행 진입점)

---

## 🧩 주요 기능 요약

### 🔐 보안 관련
| 구성 요소 | 설명 |
|-----------|------|
| `config/SecurityConfig.java` | 인증 경로, 필터 체인 설정 등 Spring Security 기본 구성 |
| `security/JwtAuthFilter.java` | 요청마다 JWT 토큰을 검증 |
| `security/JwtUtils.java` | 토큰 발급, 파싱, 유효성 검증 등 토큰 유틸리티 제공 |

### 👤 사용자 관리
| 구성 요소 | 설명 |
|-----------|------|
| `entity/User.java` | 사용자 정보를 담은 JPA 엔티티 |
| `repository/UserRepository.java` | 사용자 정보 DB 접근 처리 |
| `service/UserService.java` | 사용자 이름 수정 등의 비즈니스 로직 |
| `service/UserDetailsServiceImpl.java` | Spring Security와 연동되는 사용자 정보 제공자 |

### 🔑 인증 처리
| 구성 요소 | 설명 |
|-----------|------|
| `controller/AuthController.java` | 로그인 및 회원가입 API 처리 |
| `service/AuthService.java` | 인증 관련 로직 처리 |
| `dto/AuthRequest.java` | 로그인 요청 DTO |
| `dto/AuthResponse.java` | 로그인 응답 DTO |

### 📝 DTO
| 구성 요소 | 설명 |
|-----------|------|
| `dto/SignUpRequest.java` | 회원가입 시 필요한 사용자 입력 값 |
| `dto/UpdateNameRequest.java` | 사용자 이름 수정 요청 값 |

### 📚 API 문서화
| 구성 요소 | 설명 |
|-----------|------|
| `config/OpenApiConfig.java` | Swagger(OpenAPI 3.0) 문서 설정 제공 |

---

## 📌 프로젝트 특징

- ✅ **계층형 아키텍처** (Controller → Service → Repository)
- ✅ **DTO 기반 데이터 전송** 및 유효성 처리
- ✅ **JWT 기반 인증** 시스템 구현
- ✅ **Spring Security** 연동
- ✅ **Swagger UI**를 통한 실시간 API 문서 제공


