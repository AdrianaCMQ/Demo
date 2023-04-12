package com.tw.oquizfinal.applications.coupon;

import com.tw.oquizfinal.applications.exceptions.ProductNotExistException;
import com.tw.oquizfinal.domain.order.Order;
import com.tw.oquizfinal.domain.orderItem.OrderItem;
import com.tw.oquizfinal.domain.product.ProductServiceClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
@Component
@AllArgsConstructor
public class ThreeItemsDiscount implements DiscountStrategy{

    public static final int QUANTITY = 3;
    public static final BigDecimal DISCOUNTRATE = BigDecimal.valueOf(0.2);
    private final ProductServiceClient client;

    @Override
    public BigDecimal calculateDiscount(Order order, BigDecimal totalSum, List<OrderItem> orderItems) {

        return orderItems.stream()
                .filter(orderItem -> orderItem.getQuantity() >= QUANTITY)
                .map(orderItem ->
                    client.getProductDetail(orderItem.getProductId())
                            .orElseThrow(() -> new ProductNotExistException(orderItem.getProductId()))
                            .getPrice()
                            .multiply(BigDecimal.valueOf(orderItem.getQuantity()))
                            .multiply(DISCOUNTRATE)
                ).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
