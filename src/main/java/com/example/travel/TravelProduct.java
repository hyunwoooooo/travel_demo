package com.example.travel;

import jakarta.persistence.*;
import java.time.LocalDate;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 여행 상품 엔티티 클래스
 * 
 * JPA를 사용하여 'travel_product' 테이블과 매핑됩니다.
 * 이 클래스는 여행 상품의 정보를 저장하고 관리합니다.
 * 
 * @Entity 어노테이션은 이 클래스가 데이터베이스 테이블에 매핑되는 JPA 엔티티임을 나타냅니다.
 */
@Entity
@Schema(description = "여행 상품 엔티티")
public class TravelProduct {

    /**
     * 상품의 고유 식별자(PK)
     * 
     * @Id - 이 필드가 엔티티의 기본 키(Primary Key)임을 나타냅니다.
     * @GeneratedValue - 기본 키 값이 자동으로 생성되는 방식을 지정합니다.
     * IDENTITY 전략은 데이터베이스의 AUTO_INCREMENT를 사용합니다.
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "상품 ID", example = "1")
    private Long id;

    /**
     * 여행 상품의 이름
     */
    @Schema(description = "상품명", example = "제주도 3박 4일 패키지")
    private String name;
    
    /**
     * 여행 상품에 대한 상세 설명
     */
    @Schema(description = "상품 설명", example = "아름다운 제주도에서의 휴양 여행")
    private String description;
    
    /**
     * 여행 상품의 가격 (원 단위)
     */
    @Schema(description = "가격", example = "450000")
    private int price;
    
    /**
     * 여행 목적지 위치 정보
     */
    @Schema(description = "여행 지역", example = "제주도")
    private String location;
    
    /**
     * 여행 시작 날짜
     * 
     * LocalDate 타입은 Java 8부터 도입된 날짜 관련 API로 
     * 시간 정보 없이 날짜만 다룰 때 사용합니다.
     */
    @Schema(description = "여행 시작일", example = "2023-07-15")
    private LocalDate startDate;
    
    /**
     * 여행 종료 날짜
     */
    @Schema(description = "여행 종료일", example = "2023-07-18")
    private LocalDate endDate;

    /**
     * 기본 생성자 (JPA에서 필수)
     * 
     * JPA에서는 엔티티 클래스에 인자가 없는 기본 생성자가 반드시 필요합니다.
     * 직접 사용하지 않더라도 JPA가 내부적으로 객체를 생성할 때 사용합니다.
     */
    public TravelProduct() {}

    // === getter & setter 메소드 ===
    // 캡슐화를 위해 private 필드에 접근하기 위한 메소드들을 제공합니다.
    
    /**
     * 상품 ID를 반환합니다.
     */
    public Long getId() { return id; }

    /**
     * 상품명을 반환합니다.
     */
    public String getName() { return name; }
    
    /**
     * 상품명을 설정합니다.
     */
    public void setName(String name) { this.name = name; }

    /**
     * 상품 설명을 반환합니다.
     */
    public String getDescription() { return description; }
    
    /**
     * 상품 설명을 설정합니다.
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * 가격을 반환합니다.
     */
    public int getPrice() { return price; }
    
    /**
     * 가격을 설정합니다.
     */
    public void setPrice(int price) { this.price = price; }

    /**
     * 여행 지역을 반환합니다.
     */
    public String getLocation() { return location; }
    
    /**
     * 여행 지역을 설정합니다.
     */
    public void setLocation(String location) { this.location = location; }

    /**
     * 여행 시작일을 반환합니다.
     */
    public LocalDate getStartDate() { return startDate; }
    
    /**
     * 여행 시작일을 설정합니다.
     */
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    /**
     * 여행 종료일을 반환합니다.
     */
    public LocalDate getEndDate() { return endDate; }
    
    /**
     * 여행 종료일을 설정합니다.
     */
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
}
