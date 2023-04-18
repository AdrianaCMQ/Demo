package com.tw.oquizfinal.adapters.mapper;

import com.tw.oquizfinal.adapters.dto.OrderItemWithProduct;
import com.tw.oquizfinal.adapters.dto.request.OrderRequest;
import com.tw.oquizfinal.adapters.dto.responses.OrderResponse;
import com.tw.oquizfinal.domain.order.Order;
import com.tw.oquizfinal.domain.orderItem.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderDtoMapperTest {

    public static final String TEST_ADDRESSEE = "test addressee";
    public static final String TEST_ADDRESS = "test address";
    public static final String MOBILE = "12345678987";
    public static final int QUANTITY = 10;
    public static final BigDecimal PRICE = BigDecimal.TEN;
    public static final String PRODUCT_TITLE = "product title";
    public static final String CATEGORY = "test category";

    private final OrderItem orderItem = new OrderItem();
    private final Order order = new Order();
    private final OrderRequest orderRequest = new OrderRequest();
    private final OrderResponse orderResponse = new OrderResponse();
    private final OrderItemWithProduct orderItemWithProduct = new OrderItemWithProduct();

    @BeforeEach
    void Initialize() {
        orderItem.setItemId(1L);
        orderItem.setProductId(1L);
        orderItem.setQuantity(QUANTITY);

        order.setAddressee(TEST_ADDRESSEE);
        order.setAddress(TEST_ADDRESS);
        order.setMobile(MOBILE);
        order.setCouponId(1L);
        order.setItems(List.of(orderItem));

        orderRequest.setAddressee(TEST_ADDRESSEE);
        orderRequest.setAddress(TEST_ADDRESS);
        orderRequest.setMobile(MOBILE);
        orderRequest.setCouponId(1L);
        orderRequest.setItems(List.of(orderItem));

        orderItemWithProduct.setItemId(1L);
        orderItemWithProduct.setProductId(1L);
        orderItemWithProduct.setTitle(PRODUCT_TITLE);
        orderItemWithProduct.setPrice(PRICE);
        orderItemWithProduct.setCategory(CATEGORY);
        orderItemWithProduct.setQuantity(QUANTITY);

        orderResponse.setOrderId(1L);
        orderResponse.setAddressee(TEST_ADDRESSEE);
        orderResponse.setAddress(TEST_ADDRESS);
        orderResponse.setMobile(MOBILE);
        orderResponse.setCouponId(1L);
        orderResponse.setTotalPrice(PRICE);
        orderResponse.setOrderItems(List.of(orderItemWithProduct));
    }

    @Test
    void should_mapper_order_request_to_order_model() {
        Order model = OrderDtoMapper.MAPPER.toModel(orderRequest, Instant.now());

        assertEquals(model.getOrderId(), order.getOrderId());
        assertEquals(model.getAddressee(), order.getAddressee());
        assertEquals(model.getAddress(), order.getAddress());
        assertEquals(model.getMobile(), order.getMobile());
        assertEquals(model.getTotalPrice(), order.getTotalPrice());
        assertEquals(model.getCouponId(), order.getCouponId());
        assertEquals(model.getItems().get(0).getItemId(), order.getItems().get(0).getItemId());
        assertEquals(model.getItems().size(), order.getItems().size());
    }

    @Test
    void should_mapper_order_and_items_to_order_response() {
        order.setOrderId(1L);
        order.setTotalPrice(PRICE);
        OrderResponse response = OrderDtoMapper.MAPPER.toResponse(order, List.of(orderItemWithProduct));

        assertEquals(response.getOrderId(), orderResponse.getOrderId());
        assertEquals(response.getAddressee(), orderResponse.getAddressee());
        assertEquals(response.getAddress(), orderResponse.getAddress());
        assertEquals(response.getMobile(), orderResponse.getMobile());
        assertEquals(response.getTotalPrice(), orderResponse.getTotalPrice());
        assertEquals(response.getCouponId(), orderResponse.getCouponId());
        assertEquals(response.getOrderItems().size(), orderResponse.getOrderItems().size());
    }

    @Test
    void should_build_page_request_by_request_parameter() {
        int page = 2;
        int size = 2;
        String sortBy = "createdAt";
        String orderBy = "desc";
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.Direction.DESC, sortBy);
        PageRequest mapped = OrderDtoMapper.MAPPER.buildPageRequest(page, 2, orderBy, sortBy);
        assertEquals(pageRequest, mapped);
    }
}
