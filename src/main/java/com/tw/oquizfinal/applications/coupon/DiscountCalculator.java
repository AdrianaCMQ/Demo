package com.tw.oquizfinal.applications.coupon;

import com.tw.oquizfinal.domain.order.Order;
import com.tw.oquizfinal.domain.orderItem.OrderItem;
import com.tw.oquizfinal.domain.product.Product;
import com.tw.oquizfinal.domain.product.ProductServiceClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@AllArgsConstructor
public class DiscountCalculator {

    private final ProductServiceClient client;

    public BigDecimal getTotalPrice(Order order, List<OrderItem> orderItems) {
        return orderItems.stream().map(orderItem -> {
            Product product = client.getProductDetail(orderItem.getProductId()).get();
            return product.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
        }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
