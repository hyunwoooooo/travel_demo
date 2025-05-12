package com.example.travel.dto;

import com.example.travel.entity.TravelProduct;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 여행 상품 데이터 전송 객체(DTO)
 * 
 * 클라이언트와 서버 간의 데이터 교환을 위한 객체입니다.
 * ID 필드가 없어 생성 요청에 적합합니다.
 */
@Data
@Schema(description = "여행 상품 DTO")
public class TravelProductDto {

    @Schema(description = "상품명", example = "제주도 3박 4일 패키지", required = true)
    private String name;
    
    @Schema(description = "상품 설명", example = "아름다운 제주도에서의 휴양 여행")
    private String description;
    
    @Schema(description = "가격", example = "450000", required = true)
    private int price;
    
    @Schema(description = "여행 지역", example = "제주도")
    private String location;
    
    @Schema(description = "여행 시작일", example = "2023-07-15")
    private LocalDate startDate;
    
    @Schema(description = "여행 종료일", example = "2023-07-18")
    private LocalDate endDate;

    /**
     * DTO를 엔티티로 변환
     * 
     * @return TravelProduct 엔티티
     */
    public TravelProduct toEntity() {
        TravelProduct product = new TravelProduct();
        product.setName(this.name);
        product.setDescription(this.description);
        product.setPrice(this.price);
        product.setLocation(this.location);
        product.setStartDate(this.startDate);
        product.setEndDate(this.endDate);
        return product;
    }
} 