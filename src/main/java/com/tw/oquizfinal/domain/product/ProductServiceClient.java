package com.tw.oquizfinal.domain.product;

import com.tw.oquizfinal.infrastructure.product.dto.response.ProductDetailResponse;

import java.util.List;
import java.util.Optional;

public interface ProductServiceClient {

    Optional<Product> getProductDetail(Long id);

    List<ProductDetailResponse> getAllProducts();
}
