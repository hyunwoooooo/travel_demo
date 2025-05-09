package com.example.travel;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 여행 상품 REST 컨트롤러
 * 
 * 클라이언트의 HTTP 요청을 처리하는 컨트롤러 계층입니다.
 * @RestController 어노테이션은 이 클래스가 REST API 엔드포인트를 제공하는 컨트롤러임을 나타냅니다.
 * 
 * REST 컨트롤러의 역할:
 * 1. 클라이언트의 HTTP 요청을 받아 적절한 서비스 메소드를 호출합니다.
 * 2. 서비스의 결과를 클라이언트에게 반환합니다.
 * 3. HTTP 요청 파라미터를 검증하고 변환합니다.
 * 4. 적절한 HTTP 상태 코드와 응답을 반환합니다.
 * 
 * @RequestMapping("/products") 어노테이션은 이 컨트롤러의 모든 메소드에 대한
 * 기본 URL 경로가 '/products'임을 나타냅니다.
 */
@RestController
@RequestMapping("/products")
@Tag(name = "여행 상품 API", description = "여행 상품 CRUD API")
public class TravelProductController {

    /**
     * TravelProductService 의존성 주입
     * 
     * 컨트롤러는 서비스 계층에 의존하여 비즈니스 로직을 실행합니다.
     */
    private final TravelProductService service;

    /**
     * 생성자를 통한 의존성 주입 (Constructor Injection)
     * 
     * @param service 여행 상품 서비스
     */
    public TravelProductController(TravelProductService service) {
        this.service = service;
    }

    /**
     * 새로운 여행 상품을 생성하는 엔드포인트
     * 
     * HTTP POST 요청을 처리하여 여행 상품을 생성합니다.
     * @PostMapping 어노테이션은 이 메소드가 HTTP POST 요청을 처리함을 나타냅니다.
     * 
     * @param product 클라이언트로부터 받은 여행 상품 데이터
     * @return 생성된 여행 상품(ID 부여됨)
     * 
     * @RequestBody 어노테이션은 HTTP 요청 본문의 JSON 데이터를 Java 객체로 변환합니다.
     */
    @PostMapping
    @Operation(summary = "여행 상품 생성", description = "새로운 여행 상품을 생성합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "여행 상품 생성 성공",
                content = @Content(schema = @Schema(implementation = TravelProduct.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public TravelProduct create(@RequestBody TravelProduct product) {
        return service.save(product);
    }

    /**
     * 모든 여행 상품을 조회하는 엔드포인트
     * 
     * HTTP GET 요청을 처리하여 모든 여행 상품 목록을 반환합니다.
     * @GetMapping 어노테이션은 이 메소드가 HTTP GET 요청을 처리함을 나타냅니다.
     * 
     * @return 여행 상품 목록
     */
    @GetMapping
    @Operation(summary = "모든 여행 상품 조회", description = "등록된 모든 여행 상품 목록을 조회합니다.")
    public List<TravelProduct> getAll() {
        return service.findAll();
    }

    /**
     * ID로 특정 여행 상품을 조회하는 엔드포인트
     * 
     * HTTP GET 요청을 처리하여 특정 ID의 여행 상품을 반환합니다.
     * @GetMapping("/{id}") 어노테이션은 '/products/{id}' 형태의 URL 경로를 처리함을 나타냅니다.
     * 
     * @param id 조회할 여행 상품의 ID(URL 경로에서 추출)
     * @return 조회된 여행 상품 또는 없는 경우 null
     * 
     * @PathVariable 어노테이션은 URL 경로의 변수 부분을 메소드 매개변수로 바인딩합니다.
     */
    @GetMapping("/{id}")
    @Operation(summary = "여행 상품 상세 조회", description = "ID로 특정 여행 상품을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "여행 상품 조회 성공"),
        @ApiResponse(responseCode = "404", description = "여행 상품 없음")
    })
    public TravelProduct getOne(
            @Parameter(description = "여행 상품 ID", example = "1") 
            @PathVariable Long id) {
        return service.findById(id);
    }

    /**
     * ID로 여행 상품을 삭제하는 엔드포인트
     * 
     * HTTP DELETE 요청을 처리하여 특정 ID의 여행 상품을 삭제합니다.
     * @DeleteMapping("/{id}") 어노테이션은 '/products/{id}' 형태의 
     * URL 경로에 대한 HTTP DELETE 요청을 처리함을 나타냅니다.
     * 
     * @param id 삭제할 여행 상품의 ID(URL 경로에서 추출)
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "여행 상품 삭제", description = "ID로 특정 여행 상품을 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "여행 상품 삭제 성공"),
        @ApiResponse(responseCode = "404", description = "여행 상품 없음")
    })
    public void delete(
            @Parameter(description = "여행 상품 ID", example = "1") 
            @PathVariable Long id) {
        service.deleteById(id);
    }
}
