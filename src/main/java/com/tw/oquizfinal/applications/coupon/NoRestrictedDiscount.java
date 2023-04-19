package com.tw.oquizfinal.applications.coupon;

import com.tw.oquizfinal.domain.order.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class NoRestrictedDiscount implements DiscountStrategy{

    public static final BigDecimal DISCOUNT_AMOUNT = BigDecimal.valueOf(20);

    @Override
    public BigDecimal calculateDiscount(Order order, BigDecimal totalSum) {
        return DISCOUNT_AMOUNT;
    }
}
