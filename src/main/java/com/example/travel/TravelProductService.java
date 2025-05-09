package com.example.travel;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 여행 상품 서비스 클래스
 * 
 * 비즈니스 로직을 처리하는 서비스 계층입니다.
 * @Service 어노테이션은 이 클래스가 Spring의 서비스 빈으로 등록됨을 나타냅니다.
 * 
 * 서비스 계층의 역할:
 * 1. 컨트롤러와 리포지토리 사이의 중간 계층으로 작동합니다.
 * 2. 비즈니스 로직을 구현합니다.
 * 3. 트랜잭션 관리를 담당합니다.
 * 4. 데이터 유효성 검사 및 변환 작업을 수행합니다.
 */
@Service
public class TravelProductService {
    /**
     * TravelProductRepository 의존성 주입
     * 
     * final 키워드를 사용하여 변경 불가능한 참조를 만듭니다.
     * 이렇게 하면 객체 생성 이후에 repository 참조가 변경되지 않습니다.
     */
    private final TravelProductRepository repository;

    /**
     * 생성자를 통한 의존성 주입 (Constructor Injection)
     * 
     * Spring 4.3부터는 클래스에 생성자가 하나만 있고 
     * 그 생성자의 매개변수가 빈으로 등록된 타입이라면
     * @Autowired 어노테이션을 생략할 수 있습니다.
     * 
     * 생성자 주입의 장점:
     * 1. 필수 의존성을 명확히 표현할 수 있습니다.
     * 2. 불변성을 보장합니다.
     * 3. 테스트가 용이합니다.
     */
    public TravelProductService(TravelProductRepository repository) {
        this.repository = repository;
    }

    /**
     * 새로운 여행 상품을 저장하거나 기존 상품을 업데이트합니다.
     * 
     * @param product 저장할 여행 상품 엔티티
     * @return 저장된 여행 상품 엔티티 (ID가 부여됨)
     */
    public TravelProduct save(TravelProduct product) {
        return repository.save(product);
    }

    /**
     * 모든 여행 상품을 조회합니다.
     * 
     * @return 여행 상품 엔티티 목록
     */
    public List<TravelProduct> findAll() {
        return repository.findAll();
    }

    /**
     * ID로 특정 여행 상품을 조회합니다.
     * 
     * @param id 조회할 여행 상품의 ID
     * @return 조회된 여행 상품 또는 존재하지 않는 경우 null
     */
    public TravelProduct findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    /**
     * ID로 여행 상품을 삭제합니다.
     * 
     * @param id 삭제할 여행 상품의 ID
     */
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
