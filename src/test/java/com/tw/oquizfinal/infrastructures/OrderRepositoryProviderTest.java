package com.tw.oquizfinal.infrastructures;

import com.tw.oquizfinal.adapters.mapper.OrderDtoMapper;
import com.tw.oquizfinal.domain.order.Order;
import com.tw.oquizfinal.domain.orderItem.OrderItem;
import com.tw.oquizfinal.infrastructures.order.OrderRepositoryProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class OrderRepositoryProviderTest {
    public static final String TEST_ADDRESSEE = "test addressee";
    public static final String TEST_ADDRESS = "test address";
    public static final String MOBILE = "12345678987";
    public static final int QUANTITY = 10;
    public static final BigDecimal PRICE = BigDecimal.TEN;
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
        order.setTotalPrice(PRICE);
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

    @Nested
    class getOrders {

        @Test
        @Sql("classpath:scripts/insert_3_orders.sql")
        @Sql("classpath:scripts/insert_5_items.sql")
        @Sql("classpath:scripts/insert_5_orders_items.sql")
        void should_get_all_orders() {
            List<Order> orders = orderRepositoryProvider.findAll();

            assertNotNull(orders);
            assertEquals(orders.size(), 3);
            assertEquals(orders.get(0).getOrderId(), 1L);
            assertEquals(orders.get(1).getOrderId(), 2L);
            assertEquals(orders.get(2).getOrderId(), 3L);
        }

        @Test
        @Sql("classpath:scripts/insert_3_orders.sql")
        @Sql("classpath:scripts/insert_5_items.sql")
        @Sql("classpath:scripts/insert_5_orders_items.sql")
        void should_get_all_orders_by_page() {
            PageRequest pageRequest = OrderDtoMapper.MAPPER.buildPageRequest(1, 2, "desc", "createdAt");

            Page<Order> orders = orderRepositoryProvider.findAllByPage(pageRequest);

            assertNotNull(orders);
            Assertions.assertEquals(0, orders.getNumber());
            Assertions.assertEquals(2, orders.getSize());
            Assertions.assertEquals(Sort.by(Sort.Direction.DESC, "createdAt"), orders.getSort());
            Assertions.assertEquals(2, orders.getTotalPages());
            Assertions.assertEquals(3, orders.getTotalElements());
        }
    }
}
