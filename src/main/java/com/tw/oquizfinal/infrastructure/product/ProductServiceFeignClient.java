package com.tw.oquizfinal.infrastructure.product;

import com.tw.oquizfinal.infrastructure.product.dto.response.ProductDetailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "product-service", url = "https://fakestoreapi.com")
public interface ProductServiceFeignClient {

    @GetMapping("/products/{id}")
    ProductDetailResponse getProductDetail(@PathVariable Long id);

    @GetMapping("/products")
    List<ProductDetailResponse> getAllProducts();
}
