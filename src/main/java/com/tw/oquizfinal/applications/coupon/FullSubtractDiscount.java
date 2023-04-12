package com.tw.oquizfinal.applications.coupon;

import com.tw.oquizfinal.domain.order.Order;
import com.tw.oquizfinal.domain.orderItem.OrderItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
@Component
public class FullSubtractDiscount implements DiscountStrategy{

    public static final BigDecimal PRICE_BAR = BigDecimal.valueOf(1000);
    public static final BigDecimal DISCOUNT_AMOUNT = BigDecimal.valueOf(50);

    @Override
    public BigDecimal calculateDiscount(Order order, BigDecimal totalSum, List<OrderItem> orderItems) {
        if (totalSum.compareTo(PRICE_BAR) >= 0) {
            return totalSum.divide(PRICE_BAR, 0, RoundingMode.DOWN).multiply(DISCOUNT_AMOUNT);
        }
        return BigDecimal.ZERO;
    }
}
