package com.tw.oquizfinal.applications.coupon;

import com.tw.oquizfinal.applications.exceptions.ProductNotExistException;
import com.tw.oquizfinal.domain.order.Order;
import com.tw.oquizfinal.domain.orderItem.OrderItem;
import com.tw.oquizfinal.domain.product.Product;
import com.tw.oquizfinal.domain.product.ProductServiceClient;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@AllArgsConstructor
public class DiscountCalculator {

    public static final String FULL_SUBTRACT = "1";
    public static final String THREE_ITEMS = "2";
    public static final String NO_RESTRICTED = "3";
    private final ProductServiceClient client;
    private final FullSubtractDiscount fullSubtractDiscount;

    private final ThreeItemsDiscount threeItemsDiscount;

    private final NoRestrictedDiscount noRestrictedDiscount;

    public BigDecimal getTotalPrice(Order order) {
        List<OrderItem> orderItems = order.getItems();
        BigDecimal totalSum = getTotalSum(orderItems);
        String couponId = String.valueOf(order.getCouponId());

        return switch (couponId) {
            case FULL_SUBTRACT ->
                    totalSum.subtract(fullSubtractDiscount.calculateDiscount(order, totalSum));
            case THREE_ITEMS -> totalSum.subtract(threeItemsDiscount.calculateDiscount(order, totalSum));
            case NO_RESTRICTED ->
                    totalSum.subtract(noRestrictedDiscount.calculateDiscount(order, totalSum));
            default -> totalSum;
        };
    }

    @NotNull
    private BigDecimal getTotalSum(List<OrderItem> orderItems) {
        return orderItems.stream().map(orderItem -> {
            Product product = client.getProductDetail(orderItem.getProductId())
                    .orElseThrow(() -> new ProductNotExistException(orderItem.getProductId()));

            return product.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
        }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
