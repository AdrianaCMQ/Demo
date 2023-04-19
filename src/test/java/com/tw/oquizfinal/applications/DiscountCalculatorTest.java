package com.tw.oquizfinal.applications;

import com.tw.oquizfinal.applications.coupon.DiscountCalculator;
import com.tw.oquizfinal.applications.coupon.FullSubtractDiscount;
import com.tw.oquizfinal.applications.coupon.NoRestrictedDiscount;
import com.tw.oquizfinal.applications.coupon.ThreeItemsDiscount;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    @Mock
    private FullSubtractDiscount fullSubtractDiscount;
    @Mock
    private ThreeItemsDiscount threeItemsDiscount;
    @Mock
    private NoRestrictedDiscount noRestrictedDiscount;


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

    @ParameterizedTest
    @ValueSource(longs = {0L, 1L, 2L, 3L})
    void should_return_total_price_with_or_without_discount(long couponId) {
        order.setCouponId(couponId);
        when(client.getProductDetail(orderItem.getProductId())).thenReturn(Optional.of(product));
        when(fullSubtractDiscount.calculateDiscount(order, BigDecimal.valueOf(5000)))
                .thenReturn(BigDecimal.valueOf(250));
        when(threeItemsDiscount.calculateDiscount(order, BigDecimal.valueOf(5000)))
                .thenReturn(BigDecimal.valueOf(1000.00));
        when(noRestrictedDiscount.calculateDiscount(order, BigDecimal.valueOf(5000)))
                .thenReturn(BigDecimal.valueOf(20));

        BigDecimal totalPrice = discountCalculator.getTotalPrice(order);
        BigDecimal expectedTotalPrice = BigDecimal.ZERO;

        if (couponId == 0L) {
            expectedTotalPrice = expectedTotalPrice.add(BigDecimal.valueOf(5000));
        } else if (couponId == 1L) {
            expectedTotalPrice = expectedTotalPrice.add(BigDecimal.valueOf(4750));
        } else if (couponId == 2L) {
            expectedTotalPrice = expectedTotalPrice.add(BigDecimal.valueOf(4000.00));
        } else if (couponId == 3L) {
            expectedTotalPrice = expectedTotalPrice.add(BigDecimal.valueOf(4980));
        }

        assertEquals(expectedTotalPrice, totalPrice);
    }

    @Test
    public void should_throw_exception_when_product_does_not_exist() {
        when(client.getProductDetail(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ProductNotExistException.class,
                () -> discountCalculator.getTotalPrice(order));
        assertThat(exception.getMessage()).isEqualTo("Product doesn't exist: 1");
    }
}
