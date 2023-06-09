package com.tw.oquizfinal.infrastructures.product;

import com.tw.oquizfinal.domain.product.Product;
import com.tw.oquizfinal.domain.product.ProductServiceClient;
import com.tw.oquizfinal.infrastructures.product.dto.response.ProductDetailResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class ProductServiceFeignClientProvider implements ProductServiceClient {

    private final ProductServiceFeignClient client;

    @Override
    public Optional<Product> getProductDetail(Long id) {
        ProductDetailResponse productDetail = client.getProductDetail(id);
        if (productDetail == null) {
            return Optional.empty();
        }
        return Optional.of(new Product(productDetail.getId(), productDetail.getTitle(), productDetail.getPrice(), productDetail.getCategory()));
    }
}
