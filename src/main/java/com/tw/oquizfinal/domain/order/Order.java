package com.tw.oquizfinal.domain.order;

import com.tw.oquizfinal.adapters.dto.OrderItemWithProduct;
import com.tw.oquizfinal.domain.orderItem.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long orderId;
    private String addressee;
    private String address;
    private String mobile;
    private BigDecimal totalPrice;
    private Long couponId;
    private Instant createdAt;
    private List<OrderItem> items;
    private List<OrderItemWithProduct> orderItemWithProducts;
}
