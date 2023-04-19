package com.tw.oquizfinal.applications.coupon;

import com.tw.oquizfinal.domain.order.Order;

import java.math.BigDecimal;

public interface DiscountStrategy {
    BigDecimal calculateDiscount(Order order, BigDecimal totalSum);
}
