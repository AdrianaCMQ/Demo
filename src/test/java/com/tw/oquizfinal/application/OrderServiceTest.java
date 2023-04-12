package com.tw.oquizfinal.application;

import com.tw.oquizfinal.applications.OrderService;
import com.tw.oquizfinal.applications.coupon.DiscountCalculator;
import com.tw.oquizfinal.applications.exceptions.OrderItemIsEmptyException;
import com.tw.oquizfinal.domain.order.Order;
import com.tw.oquizfinal.domain.order.OrderRepository;
import com.tw.oquizfinal.domain.orderItem.OrderItem;
import com.tw.oquizfinal.domain.product.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static com.tw.oquizfinal.adapter.mapper.OrderDtoMapper.MAPPER;
import static java.util.Arrays.asList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderService orderService;
    @Mock
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
        order.setCouponId(1L);
        order.setTotalPrice(BigDecimal.TEN);
        order.setCreatedAt(Instant.now());
        order.setItems(List.of(orderItem1, orderItem2));

    }

    @Test
    void shouldReturnOrderWhenCreateOrder() {
        when(discountCalculator.getTotalPrice(order, List.of(orderItem1, orderItem2))).thenReturn(BigDecimal.TEN);
        when(orderRepository.saveOrder(order)).thenReturn(order);

        Order orderReturn = orderService.createOrder(order, List.of(orderItem1, orderItem2));

        assertEquals(order.getOrderId(),orderReturn.getOrderId());
        assertEquals(order.getAddressee(),orderReturn.getAddressee());
        assertEquals(order.getAddress(),orderReturn.getAddress());
        assertEquals(order.getMobile(),orderReturn.getMobile());
        assertEquals(order.getTotalPrice(),orderReturn.getTotalPrice());
        assertEquals(order.getCreatedAt(),orderReturn.getCreatedAt());
        assertEquals(List.of(orderItem1, orderItem2), orderReturn.getItems());
    }
    
    @Nested
    class getAllOrders {
        List<Order> orders = asList(
                Order.builder().orderId(1L).addressee("addressee").address("address").mobile("11111111111")
                        .totalPrice(BigDecimal.valueOf(108.88)).createdAt(Instant.now()).items(List.of(orderItem1, orderItem2)).build(),
                Order.builder().orderId(2L).addressee("addressee").address("address").mobile("11111111111")
                        .totalPrice(BigDecimal.valueOf(208.88)).createdAt(Instant.now().plusSeconds(1)).items(List.of(orderItem1, orderItem2)).build(),
                Order.builder().orderId(3L).addressee("addressee").address("address").mobile("11111111111")
                        .totalPrice(BigDecimal.valueOf(308.88)).createdAt(Instant.now().plusSeconds(2)).items(List.of(orderItem1, orderItem2)).build()
        );
        PageRequest pageRequest = MAPPER.buildPageRequest(1, 5, "createdAt", "desc");

        @Test
        void shouldReturnEmptyPageWhenHasNoData() {
            when(orderRepository.findAll(pageRequest)).thenReturn(new PageImpl<>(Collections.emptyList(), pageRequest, 0));

            Page<Order> orderList = orderService.getAllOrders(pageRequest);

            assertNotNull(orderList);
            Assertions.assertEquals(0, orderList.getTotalElements());
            Assertions.assertEquals(0, orderList.getNumber());
            Assertions.assertEquals(0, orderList.getTotalPages());
            Assertions.assertEquals(5, orderList.getSize());
        }

        @Test
        void shouldReturnTwoOrderWhenHasThreeDataAndPageSizeIs2() {
            PageRequest page = MAPPER.buildPageRequest(1, 2, "createdAt", "desc");

            when(orderRepository.findAll(page)).thenReturn(new PageImpl<>(orders, page, 3));

            Page<Order> orderPage = orderService.getAllOrders(page);

            assertNotNull(orderPage);
            Assertions.assertEquals(3, orderPage.getTotalElements());
            Assertions.assertEquals(0, orderPage.getNumber());
            Assertions.assertEquals(2, orderPage.getTotalPages());
            Assertions.assertEquals(2, orderPage.getSize());
            Assertions.assertEquals(3, orderPage.getContent().size());
        }

        @Test
        void shouldThrowExceptionWhenOrderItemIsEmpty() {
            List<Order> orders = Collections.singletonList(
                    Order.builder().orderId(1L).addressee("addressee").address("address").mobile("11111111111")
                            .totalPrice(BigDecimal.valueOf(108.88)).createdAt(Instant.now()).items(Collections.emptyList()).build()
            );
            PageRequest page = MAPPER.buildPageRequest(1, 2, "createdAt", "desc");

            when(orderRepository.findAll(page)).thenReturn(new PageImpl<>(orders, page, 3));

            Exception exception = assertThrows(OrderItemIsEmptyException.class,
                    () -> orderService.getAllOrders(page));
            assertThat(exception.getMessage()).isEqualTo("OrderItem doesn't exist for order: 1");
        }
    }

}
