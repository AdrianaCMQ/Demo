package com.tw.oquizfinal.infrastructures.product.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductDetailResponse {
    private Long id;
    private String title;
    private BigDecimal price;
    private String category;

}
