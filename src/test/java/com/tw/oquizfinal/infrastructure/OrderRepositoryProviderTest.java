package com.tw.oquizfinal.infrastructure;

import com.tw.oquizfinal.adapter.mapper.OrderDtoMapper;
import com.tw.oquizfinal.domain.order.Order;
import com.tw.oquizfinal.domain.orderItem.OrderItem;
import com.tw.oquizfinal.infrastructure.order.OrderRepositoryProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
    @Autowired
    private OrderRepositoryProvider orderRepositoryProvider;

    private final Order order = new Order();
    private final OrderItem orderItem1 = new OrderItem();
    private final OrderItem orderItem2 = new OrderItem();

    @BeforeEach
    void Initialize() {
        orderItem1.setQuantity(10);
        orderItem1.setProductId(1L);
        orderItem2.setQuantity(20);
        orderItem2.setProductId(2L);

        order.setOrderId(4L);
        order.setAddressee("addressee");
        order.setAddress("address");
        order.setMobile("12345678987");
        order.setTotalPrice(BigDecimal.valueOf(800));
        order.setCouponId(1L);
        order.setCreatedAt(Instant.now());
        order.setItems(List.of(orderItem1, orderItem2));
    }

    @Test
    void shouldReturnOrderEntityWhenSaveOrder() {
        Order saveOrder = orderRepositoryProvider.saveOrder(order);

        assertEquals(order.getOrderId(),saveOrder.getOrderId());
        assertEquals(order.getAddressee(),saveOrder.getAddressee());
        assertEquals(order.getAddress(),saveOrder.getAddress());
        assertEquals(order.getMobile(),saveOrder.getMobile());
        assertEquals(order.getMobile(),saveOrder.getMobile());
        assertEquals(order.getItems().size(),saveOrder.getItems().size());
        assertEquals(order.getItems().get(0).getProductId(),saveOrder.getItems().get(0).getProductId());
        assertEquals(order.getTotalPrice(),saveOrder.getTotalPrice());
    }

    @Test
    @Sql("classpath:scripts/insert_3_orders.sql")
    @Sql("classpath:scripts/insert_5_items.sql")
    @Sql("classpath:scripts/insert_5_orders_items.sql")
    void shouldFindAllOrdersByPageCorrectly() {
        PageRequest pageRequest = OrderDtoMapper.MAPPER.buildPageRequest(1, 2, "createdAt", "desc");

        Page<Order> orders = orderRepositoryProvider.findAll(pageRequest);

        assertNotNull(orders);
        Assertions.assertEquals(0, orders.getNumber());
        Assertions.assertEquals(2, orders.getSize());
        Assertions.assertEquals(Sort.by(Sort.Direction.DESC, "createdAt"), orders.getSort());
        Assertions.assertEquals(2, orders.getTotalPages());
        Assertions.assertEquals(3, orders.getTotalElements());
    }

    @Test
    @Sql("classpath:scripts/insert_3_orders.sql")
    @Sql("classpath:scripts/insert_5_items.sql")
    @Sql("classpath:scripts/insert_5_orders_items.sql")
    void shouldFindAllOrdersByDesc() {
        PageRequest pageRequest = OrderDtoMapper.MAPPER.buildPageRequest(1, 10, "mobile", "desc");

        Page<Order> orders = orderRepositoryProvider.findAll(pageRequest);

        assertNotNull(orders);
        Assertions.assertEquals(Sort.by(Sort.Direction.DESC, "mobile"), orders.getSort());
        Assertions.assertEquals(1, orders.getTotalPages());
        Assertions.assertEquals(3, orders.getTotalElements());
        Assertions.assertEquals(3, orders.getContent().get(0).getOrderId());
    }

    @Test
    @Sql("classpath:scripts/insert_3_orders.sql")
    @Sql("classpath:scripts/insert_5_items.sql")
    @Sql("classpath:scripts/insert_5_orders_items.sql")
    void shouldFindAllOrdersByAsc() {
        PageRequest pageRequest = OrderDtoMapper.MAPPER.buildPageRequest(1, 10, "mobile", "asc");

        Page<Order> orders = orderRepositoryProvider.findAll(pageRequest);

        assertNotNull(orders);
        Assertions.assertEquals(Sort.by(Sort.Direction.ASC, "mobile"), orders.getSort());
        Assertions.assertEquals(1, orders.getTotalPages());
        Assertions.assertEquals(3, orders.getTotalElements());
        Assertions.assertEquals(1, orders.getContent().get(0).getOrderId());
    }
}