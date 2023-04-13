package com.tw.oquizfinal.domain.product;

import java.util.Optional;

public interface ProductServiceClient {
    Optional<Product> getProductDetail(Long id);
}
