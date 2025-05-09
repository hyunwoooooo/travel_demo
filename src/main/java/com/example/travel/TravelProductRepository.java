package com.example.travel;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 여행 상품 리포지토리 인터페이스
 * 
 * Spring Data JPA의 JpaRepository를 상속받아 데이터베이스 조작 기능을 제공합니다.
 * JpaRepository<엔티티클래스, ID타입> 형태로 제네릭을 지정합니다.
 * 
 * 이 인터페이스를 통해 다음과 같은 기본 CRUD 메소드를 사용할 수 있습니다:
 * - save(): 엔티티 저장 및 수정
 * - findById(): ID로 엔티티 조회
 * - findAll(): 모든 엔티티 조회
 * - deleteById(): ID로 엔티티 삭제
 * - 그 외 다양한 메소드들...
 * 
 * 특별한 쿼리가 필요할 경우, 여기에 메소드를 추가하여 쿼리 메소드 또는 
 * @Query 어노테이션을 사용할 수 있습니다.
 * 
 * 예:
 * - List<TravelProduct> findByLocation(String location);
 * - List<TravelProduct> findByPriceLessThan(int price);
 */
public interface TravelProductRepository extends JpaRepository<TravelProduct, Long> {
    // Spring Data JPA는 인터페이스만 정의해도 자동으로 구현체를 생성해줍니다.
    // 기본 CRUD 기능 외에 필요한 메소드가 있다면 여기에 선언하면 됩니다.
}
