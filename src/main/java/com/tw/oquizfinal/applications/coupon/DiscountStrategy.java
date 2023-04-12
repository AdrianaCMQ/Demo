package com.tw.oquizfinal.applications.coupon;

import com.tw.oquizfinal.domain.order.Order;
import com.tw.oquizfinal.domain.orderItem.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public interface DiscountStrategy {
    BigDecimal calculateDiscount(Order order, BigDecimal totalSum, List<OrderItem> orderItems);
}
