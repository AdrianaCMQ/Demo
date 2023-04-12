package com.tw.oquizfinal.application;

import com.tw.oquizfinal.applications.coupon.DiscountCalculator;
import com.tw.oquizfinal.applications.coupon.FullSubtractDiscount;
import com.tw.oquizfinal.applications.coupon.ThreeItemsDiscount;
import com.tw.oquizfinal.applications.coupon.UnrestrictedDiscount;
import com.tw.oquizfinal.applications.exceptions.ProductNotExistException;
import com.tw.oquizfinal.domain.order.Order;
import com.tw.oquizfinal.domain.orderItem.OrderItem;
import com.tw.oquizfinal.domain.product.Product;
import com.tw.oquizfinal.domain.product.ProductServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class DiscountCalculatorTest {

    @Mock
    private ProductServiceClient client;
    @Mock
    private FullSubtractDiscount fullSubtractDiscount;
    @Mock
    private ThreeItemsDiscount threeItemsDiscount;
    @Mock
    private UnrestrictedDiscount unrestrictedDiscount;

    @InjectMocks
    private DiscountCalculator discountCalculator;

    private final Order order = new Order();
    private final OrderItem orderItem1 = new OrderItem();
    private final OrderItem orderItem2 = new OrderItem();
    private final Product product1 = new Product();
    private final Product product2 = new Product();
    @BeforeEach
    void Initialize() {
        orderItem1.setQuantity(10);
        orderItem1.setProductId(1L);
        orderItem2.setQuantity(20);
        orderItem2.setProductId(2L);

        product1.setId(1L);
        product1.setTitle("Jacket");
        product1.setPrice(BigDecimal.valueOf(100));
        product1.setCategory("category1");
        product2.setId(2L);
        product2.setTitle("shoes");
        product2.setPrice(BigDecimal.valueOf(200));
        product2.setCategory("category2");

        order.setOrderId(1L);
        order.setAddressee("addressee");
        order.setAddress("address");
        order.setMobile("12345678987");
        order.setCreatedAt(Instant.now());

    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void shouldCalculateTotalPriceCorrectly(long couponId) {
        order.setCouponId(couponId);
        when(client.getProductDetail(orderItem1.getProductId())).thenReturn(
                Optional.of(product1));
        when(client.getProductDetail(orderItem2.getProductId())).thenReturn(
                Optional.of(product2));
        when(fullSubtractDiscount.calculateDiscount(order, BigDecimal.valueOf(5000), List.of(orderItem1, orderItem2)))
                .thenReturn(BigDecimal.valueOf(250));
        when(threeItemsDiscount.calculateDiscount(order, BigDecimal.valueOf(5000), List.of(orderItem1, orderItem2)))
                .thenReturn(BigDecimal.valueOf(250));
        when(unrestrictedDiscount.calculateDiscount(order, BigDecimal.valueOf(5000), List.of(orderItem1, orderItem2)))
                .thenReturn(BigDecimal.valueOf(20));

        BigDecimal totalPrice = discountCalculator.getTotalPrice(order, List.of(orderItem1, orderItem2));


        BigDecimal expectedTotalPrice = BigDecimal.ZERO;

        if (couponId == 1L) {
            expectedTotalPrice = expectedTotalPrice.add(BigDecimal.valueOf(4750));
        } else if (couponId == 2L) {
            expectedTotalPrice = expectedTotalPrice.add(BigDecimal.valueOf(4750));
        } else if (couponId == 3L) {
            expectedTotalPrice = expectedTotalPrice.add(BigDecimal.valueOf(4980));
        }

        assertEquals(expectedTotalPrice, totalPrice);
    }

    @Test
    public void testFindProductById() {
        when(client.getProductDetail(1L)).thenReturn(Optional.of(product1));
        Product foundProduct = discountCalculator.findProductById(1L);
        assertNotNull(foundProduct);
        assertEquals(product1.getId(), foundProduct.getId());
        assertEquals(product1.getPrice(), foundProduct.getPrice());
    }

    @Test
    public void testFindProductByIdNotExist() {
        when(client.getProductDetail(1L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ProductNotExistException.class,
                () -> discountCalculator.findProductById(1L));
        assertThat(exception.getMessage()).isEqualTo("Product doesn't exist: 1");
    }
}
