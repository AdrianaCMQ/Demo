package com.tw.oquizfinal.applications.coupon;

import com.tw.oquizfinal.domain.order.Order;
import com.tw.oquizfinal.domain.orderItem.OrderItem;
import com.tw.oquizfinal.domain.product.Product;
import com.tw.oquizfinal.domain.product.ProductServiceClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
@AllArgsConstructor
public class DiscountCalculator {

    public static final BigDecimal PRICE_BAR = BigDecimal.valueOf(1000);
    public static final BigDecimal DISCOUNT_AMOUNT = BigDecimal.valueOf(50);
    public static final int QUALIFY_QUANTITY = 3;
    public static final BigDecimal DISCOUNT_RATE = BigDecimal.valueOf(0.2);
    private final ProductServiceClient client;

    public BigDecimal getTotalPrice(Order order, List<OrderItem> orderItems) {

        BigDecimal totalSum = orderItems.stream().map(orderItem -> {
            Product product = client.getProductDetail(orderItem.getProductId()).get();
            return product.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
        }).reduce(BigDecimal.ZERO, BigDecimal::add);

        if (order.getCouponId() == 1) {
            BigDecimal discount = BigDecimal.ZERO;
            if (totalSum.compareTo(PRICE_BAR) >= 0) {
                discount = totalSum.divide(PRICE_BAR, 0, RoundingMode.DOWN).multiply(DISCOUNT_AMOUNT);
            }
            return totalSum.subtract(discount);
        }

        if (order.getCouponId() == 2) {
            BigDecimal discount = orderItems.stream()
                    .filter(orderItem -> orderItem.getQuantity() >= QUALIFY_QUANTITY)
                    .map(orderItem ->
                            client.getProductDetail(orderItem.getProductId())
                                    .get()
                                    .getPrice()
                                    .multiply(BigDecimal.valueOf(orderItem.getQuantity()))
                                    .multiply(DISCOUNT_RATE)
                    ).reduce(BigDecimal.ZERO, BigDecimal::add);
            return totalSum.subtract(discount);
        }
        return totalSum;
    }
}
