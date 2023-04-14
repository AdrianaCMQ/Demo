package com.tw.oquizfinal.applications;

import com.tw.oquizfinal.applications.coupon.DiscountCalculator;
import com.tw.oquizfinal.domain.order.Order;
import com.tw.oquizfinal.domain.orderItem.OrderItem;
import com.tw.oquizfinal.domain.product.Product;
import com.tw.oquizfinal.domain.product.ProductServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class DiscountCalculatorTest {
    public static final String TEST_PRODUCT = "test product";
    public static final BigDecimal PRODUCT_PRICE = BigDecimal.valueOf(500);
    public static final String TEST_CATEGORY = "test category";

    @InjectMocks
    private DiscountCalculator discountCalculator;
    @Mock
    private ProductServiceClient client;

    public static final String TEST_ADDRESSEE = "test addressee";
    public static final String TEST_ADDRESS = "test address";
    public static final String MOBILE = "12345678987";
    public static final int QUANTITY = 10;

    private final OrderItem orderItem = new OrderItem();
    private final Order order = new Order();
    private final Product product = new Product();

    @BeforeEach
    void Initialize() {
        orderItem.setItemId(1L);
        orderItem.setProductId(1L);
        orderItem.setQuantity(QUANTITY);

        order.setOrderId(1L);
        order.setAddressee(TEST_ADDRESSEE);
        order.setAddress(TEST_ADDRESS);
        order.setMobile(MOBILE);
        order.setItems(List.of(orderItem));

        product.setId(1L);
        product.setTitle(TEST_PRODUCT);
        product.setPrice(PRODUCT_PRICE);
        product.setCategory(TEST_CATEGORY);
    }

    @Test
    void should_return_total_price_with_no_discount() {
        order.setCouponId(0L);
        when(client.getProductDetail(orderItem.getProductId())).thenReturn(Optional.of(product));

        BigDecimal totalPrice = discountCalculator.getTotalPrice(order, List.of(orderItem));
        BigDecimal expectedTotalPrice = BigDecimal.valueOf(5000);

        assertEquals(totalPrice, expectedTotalPrice);
    }

    @Test
    void should_return_total_price_with_full_subtract_discount() {
        order.setCouponId(1L);
        when(client.getProductDetail(orderItem.getProductId())).thenReturn(Optional.of(product));

        BigDecimal totalPrice = discountCalculator.getTotalPrice(order, List.of(orderItem));
        BigDecimal expectedTotalPrice = BigDecimal.valueOf(4750);

        assertEquals(totalPrice, expectedTotalPrice);
    }

    @Test
    void should_return_total_price_with_three_items_discount() {
        order.setCouponId(2L);
        when(client.getProductDetail(orderItem.getProductId())).thenReturn(Optional.of(product));

        BigDecimal totalPrice = discountCalculator.getTotalPrice(order, List.of(orderItem));
        BigDecimal expectedTotalPrice = BigDecimal.valueOf(4000.00);

        assertEquals(totalPrice, expectedTotalPrice);
    }
}
