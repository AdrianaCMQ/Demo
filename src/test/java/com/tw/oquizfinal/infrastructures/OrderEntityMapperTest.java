package com.tw.oquizfinal.infrastructures;

import com.tw.oquizfinal.domain.order.Order;
import com.tw.oquizfinal.domain.orderItem.OrderItem;
import com.tw.oquizfinal.infrastructures.order.entity.OrderEntity;
import com.tw.oquizfinal.infrastructures.order.mapper.OrderEntityMapper;
import com.tw.oquizfinal.infrastructures.orderItem.entity.OrderItemEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderEntityMapperTest {

    public static final String TEST_ADDRESSEE = "test addressee";
    public static final String TEST_ADDRESS = "test address";
    public static final String MOBILE = "12345678987";
    public static final int QUANTITY = 10;
    public static final BigDecimal PRICE = BigDecimal.TEN;

    private final OrderItem orderItem = new OrderItem();
    private final Order order = new Order();
    private final OrderItemEntity orderItemEntity = new OrderItemEntity();
    private final OrderEntity orderEntity = new OrderEntity();

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
        order.setItems(List.of(orderItem));

        orderItemEntity.setItemId(1L);
        orderItemEntity.setProductId(1L);
        orderItemEntity.setQuantity(QUANTITY);

        orderEntity.setOrderId(1L);
        orderEntity.setAddressee(TEST_ADDRESSEE);
        orderEntity.setAddress(TEST_ADDRESS);
        orderEntity.setMobile(MOBILE);
        orderEntity.setTotalPrice(PRICE);
        orderEntity.setCouponId(1L);
        orderEntity.setItems(List.of(orderItemEntity));
    }
    @Test
    void should_return_entity_from_order() {
        OrderEntity entity = OrderEntityMapper.MAPPER.toEntity(order);

        assertEquals(entity.getOrderId(), orderEntity.getOrderId());
        assertEquals(entity.getAddressee(), orderEntity.getAddressee());
        assertEquals(entity.getAddress(), orderEntity.getAddress());
        assertEquals(entity.getMobile(), orderEntity.getMobile());
        assertEquals(entity.getTotalPrice(), orderEntity.getTotalPrice());
        assertEquals(entity.getCouponId(), orderEntity.getCouponId());
        assertEquals(entity.getItems().get(0).getItemId(), orderEntity.getItems().get(0).getItemId());
        assertEquals(entity.getItems().size(), orderEntity.getItems().size());
    }

    @Test
    void should_return_model_from_order_entity() {
        Order model = OrderEntityMapper.MAPPER.toModel(orderEntity);

        assertEquals(model.getOrderId(), order.getOrderId());
        assertEquals(model.getAddressee(), order.getAddressee());
        assertEquals(model.getAddress(), order.getAddress());
        assertEquals(model.getMobile(), order.getMobile());
        assertEquals(model.getTotalPrice(), order.getTotalPrice());
        assertEquals(model.getCouponId(), order.getCouponId());
        assertEquals(model.getItems().get(0).getItemId(), order.getItems().get(0).getItemId());
        assertEquals(model.getItems().size(), order.getItems().size());
    }
}
