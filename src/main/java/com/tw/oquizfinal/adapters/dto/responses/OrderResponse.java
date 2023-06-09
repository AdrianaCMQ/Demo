package com.tw.oquizfinal.adapters.dto.responses;

import com.tw.oquizfinal.adapters.dto.OrderItemWithProduct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderResponse {
    private String addressee;
    private String address;
    private String mobile;
    private Instant createdAt;
    private Long orderId;
    private BigDecimal totalPrice;
    private Long couponId;
    private List<OrderItemWithProduct> orderItemWithProducts;
}
