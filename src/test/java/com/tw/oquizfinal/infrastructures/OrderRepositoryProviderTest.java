package com.tw.oquizfinal.infrastructures;

import com.tw.oquizfinal.domain.order.Order;
import com.tw.oquizfinal.domain.orderItem.OrderItem;
import com.tw.oquizfinal.infrastructures.order.OrderRepositoryProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class OrderRepositoryProviderTest {
    public static final String TEST_ADDRESSEE = "test addressee";
    public static final String TEST_ADDRESS = "test address";
    public static final String MOBILE = "12345678987";
    public static final int QUANTITY = 10;
    @Autowired
    private OrderRepositoryProvider orderRepositoryProvider;

    private final Order order = new Order();
    private final OrderItem orderItem = new OrderItem();

    @BeforeEach
    void Initialize() {
        orderItem.setItemId(1L);
        orderItem.setProductId(1L);
        orderItem.setQuantity(QUANTITY);

        order.setOrderId(1L);
        order.setAddressee(TEST_ADDRESSEE);
        order.setAddress(TEST_ADDRESS);
        order.setMobile(MOBILE);
        order.setTotalPrice(BigDecimal.TEN);
        order.setCouponId(1L);
        order.setCreatedAt(Instant.now());
        order.setItems(List.of(orderItem));
    }

    @Test
    void should_return_order_correctly_when_save_order() {
        Order savedOrder = orderRepositoryProvider.save(order);

        assertEquals(savedOrder.getOrderId(), order.getOrderId());
        assertEquals(savedOrder.getAddressee(), order.getAddressee());
        assertEquals(savedOrder.getAddress(), order.getAddress());
        assertEquals(savedOrder.getMobile(), order.getMobile());
        assertEquals(savedOrder.getTotalPrice(), order.getTotalPrice());
        assertEquals(savedOrder.getCouponId(), order.getCouponId());
        assertEquals(savedOrder.getCreatedAt(), order.getCreatedAt());
        assertEquals(savedOrder.getItems().get(0).getItemId(), order.getItems().get(0).getItemId());
        assertEquals(savedOrder.getItems().size(), order.getItems().size());
    }
}
