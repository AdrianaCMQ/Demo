package com.tw.oquizfinal.applications.coupon;

import com.tw.oquizfinal.domain.order.Order;
import com.tw.oquizfinal.domain.orderItem.OrderItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class NoRestrictedDiscount implements DiscountStrategy{

    public static final BigDecimal DISCOUNT_AMOUNT = BigDecimal.valueOf(20);

    @Override
    public BigDecimal calculateDiscount(Order order, BigDecimal totalSum, List<OrderItem> orderItems) {
        return DISCOUNT_AMOUNT;
    }
}
