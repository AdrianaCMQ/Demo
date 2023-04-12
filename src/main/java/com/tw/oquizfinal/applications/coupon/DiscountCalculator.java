package com.tw.oquizfinal.applications.coupon;

import com.tw.oquizfinal.applications.exceptions.ProductNotExistException;
import com.tw.oquizfinal.domain.order.Order;
import com.tw.oquizfinal.domain.orderItem.OrderItem;
import com.tw.oquizfinal.domain.product.Product;
import com.tw.oquizfinal.domain.product.ProductServiceClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class DiscountCalculator {
    public static final String FULL_SUBTRACT = "1";
    public static final String THREE_ITEMS = "2";
    public static final String UNRESTRICTED = "3";
    private final FullSubtractDiscount fullSubtractDiscount;

    private final ThreeItemsDiscount threeItemsDiscount;

    private final UnrestrictedDiscount unrestrictedDiscount;

    private final ProductServiceClient client;

    public BigDecimal getTotalPrice(Order order, List<OrderItem> orderItems) {
        BigDecimal totalSum = orderItems.stream().map(orderItem ->
                findProductById(orderItem.getProductId()).getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()))
        ).reduce(BigDecimal.ZERO, BigDecimal::add);
        String couponId = String.valueOf(order.getCouponId());

        switch (couponId) {
            case FULL_SUBTRACT:

                return totalSum.subtract(fullSubtractDiscount.calculateDiscount(order, totalSum, orderItems));
            case THREE_ITEMS:

                return totalSum.subtract(threeItemsDiscount.calculateDiscount(order, totalSum, orderItems));
            case UNRESTRICTED:

                return totalSum.subtract(unrestrictedDiscount.calculateDiscount(order, totalSum, orderItems));
            default:

                return totalSum;
        }
    }

    public Product findProductById(Long productId) {

        Optional<Product> productDetail = client.getProductDetail(productId);
        return productDetail.orElseThrow(() -> new ProductNotExistException(productId));
    }
}
