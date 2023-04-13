package com.tw.oquizfinal.infrastructures.product;

import com.tw.oquizfinal.infrastructures.product.dto.response.ProductDetailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "https://fakestoreapi.com")
public interface ProductServiceFeignClient {

    @GetMapping("/products/{id}")
    ProductDetailResponse getProductDetail(@PathVariable Long id);
}
