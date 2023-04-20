package com.tw.oquizfinal.applications;

import com.tw.oquizfinal.adapters.dto.OrderItemWithProduct;
import com.tw.oquizfinal.adapters.mapper.OrderDtoMapper;
import com.tw.oquizfinal.applications.coupon.DiscountCalculator;
import com.tw.oquizfinal.domain.order.Order;
import com.tw.oquizfinal.domain.order.OrderRepository;
import com.tw.oquizfinal.domain.orderItem.OrderItem;
import com.tw.oquizfinal.domain.product.Product;
import com.tw.oquizfinal.domain.product.ProductServiceClient;
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
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    @Mock
    private ProductServiceClient client;

    public static final String TEST_ADDRESSEE = "test addressee";
    public static final String TEST_ADDRESS = "test address";
    public static final String MOBILE = "12345678987";
    public static final int QUANTITY = 10;
    public static final BigDecimal PRICE = BigDecimal.TEN;
    public static final BigDecimal PRODUCT_PRICE = BigDecimal.valueOf(500);
    public static final String TEST_CATEGORY = "test category";
    public static final String PRODUCT_TITLE = "product title";
    public static final String CATEGORY = "test category";

    private final OrderItem orderItem = new OrderItem();
    private final Order order = new Order();
    private final Product product = new Product();
    private final OrderItemWithProduct orderItemWithProduct = new OrderItemWithProduct();


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

        product.setId(1L);
        product.setTitle(PRODUCT_TITLE);
        product.setPrice(PRODUCT_PRICE);
        product.setCategory(TEST_CATEGORY);

        orderItemWithProduct.setItemId(1L);
        orderItemWithProduct.setProductId(1L);
        orderItemWithProduct.setTitle(PRODUCT_TITLE);
        orderItemWithProduct.setPrice(PRICE);
        orderItemWithProduct.setCategory(CATEGORY);
        orderItemWithProduct.setQuantity(QUANTITY);

        order.setOrderItemWithProducts(List.of(orderItemWithProduct));
    }

    @Test
    void should_return_order_when_save_it() {
        when(discountCalculator.getTotalPrice(order)).thenReturn(PRICE);
        when(orderRepository.save(order)).thenReturn(order);
        when(client.getProductDetail(1L)).thenReturn(Optional.of(product));

        Order savedOrder = orderService.save(order);

        assertEquals(order.getOrderId(), savedOrder.getOrderId());
        assertEquals(order.getAddressee(), savedOrder.getAddressee());
        assertEquals(order.getAddress(), savedOrder.getAddress());
        assertEquals(order.getMobile(), savedOrder.getMobile());
        assertEquals(order.getTotalPrice(), savedOrder.getTotalPrice());
        assertEquals(order.getCreatedAt(), savedOrder.getCreatedAt());
        assertEquals(order.getItems(), savedOrder.getItems());
        assertEquals(order.getOrderItemWithProducts(), savedOrder.getOrderItemWithProducts());
    }

    @Test
    void should_return_items_with_product_info() {
        when(discountCalculator.getTotalPrice(order)).thenReturn(PRICE);
        when(orderRepository.save(order)).thenReturn(order);
        when(client.getProductDetail(1L)).thenReturn(Optional.of(product));

        Order savedOrder = orderService.save(order);

        assertEquals(1, savedOrder.getOrderItemWithProducts().size());
        assertEquals(orderItem.getItemId(), savedOrder.getOrderItemWithProducts().get(0).getItemId());
        assertEquals(orderItem.getProductId(), savedOrder.getOrderItemWithProducts().get(0).getProductId());
        assertEquals(orderItem.getQuantity(), savedOrder.getOrderItemWithProducts().get(0).getQuantity());
        assertEquals(product.getTitle(), savedOrder.getOrderItemWithProducts().get(0).getTitle());
        assertEquals(product.getPrice(), savedOrder.getOrderItemWithProducts().get(0).getPrice());
        assertEquals(product.getCategory(), savedOrder.getOrderItemWithProducts().get(0).getCategory());
    }
    @Nested
    class getOrders {

        List<Order> orders = asList(
                Order.builder().orderId(1L).addressee(TEST_ADDRESSEE).address(TEST_ADDRESS).mobile(MOBILE)
                        .totalPrice(PRICE).createdAt(Instant.now()).items(List.of(orderItem)).build(),
                Order.builder().orderId(2L).addressee(TEST_ADDRESSEE).address(TEST_ADDRESS).mobile(MOBILE)
                        .totalPrice(PRICE.add(BigDecimal.ONE)).createdAt(Instant.now().plusSeconds(1)).items(List.of(orderItem)).build(),
                Order.builder().orderId(3L).addressee(TEST_ADDRESSEE).address(TEST_ADDRESS).mobile(MOBILE)
                        .totalPrice(PRICE.add(BigDecimal.TEN)).createdAt(Instant.now().plusSeconds(2)).items(List.of(orderItem)).build()
        );

        int page = 1;
        int size = 2;
        String sortBy = "createdAt";
        String orderBy = "desc";
        PageRequest pageRequest = OrderDtoMapper.MAPPER.buildPageRequest(page, size, orderBy, sortBy);

        @Test
        void should_get_empty_page_when_has_no_order() {
            when(orderRepository.findAllByPage(pageRequest)).thenReturn(new PageImpl<>(Collections.emptyList(), pageRequest, 0));
            when(client.getProductDetail(1L)).thenReturn(Optional.of(product));

            Page<Order> orderList = orderService.getOrdersByPage(pageRequest);

            assertNotNull(orderList);
            Assertions.assertEquals(0, orderList.getTotalElements());
            Assertions.assertEquals(0, orderList.getNumber());
            Assertions.assertEquals(0, orderList.getTotalPages());
            Assertions.assertEquals(2, orderList.getSize());
        }

        @Test
        void should_return_2_pages_when_has_three_data_and_page_size_is_2() {
            when(orderRepository.findAllByPage(pageRequest)).thenReturn(new PageImpl<>(orders, pageRequest, 3));
            when(client.getProductDetail(1L)).thenReturn(Optional.of(product));

            Page<Order> orderPage = orderService.getOrdersByPage(pageRequest);

            assertNotNull(orderPage);
            Assertions.assertEquals(3, orderPage.getTotalElements());
            Assertions.assertEquals(0, orderPage.getNumber());
            Assertions.assertEquals(2, orderPage.getTotalPages());
            Assertions.assertEquals(2, orderPage.getSize());
            Assertions.assertEquals(3, orderPage.getContent().size());
        }

        @Test
        void should_return_orders_with_order_items_and_product_info() {
            when(orderRepository.findAllByPage(pageRequest)).thenReturn(new PageImpl<>(orders, pageRequest, 3));
            when(client.getProductDetail(1L)).thenReturn(Optional.of(product));

            List<Order> orderList = orderService.getOrdersByPage(pageRequest).getContent();

            assertNotNull(orderList);
            Assertions.assertEquals(orderList.size(), 3);
            Assertions.assertEquals(orderList.get(0).getItems(), orderList.get(0).getItems());
            Assertions.assertEquals(orderList.get(1).getItems(), orderList.get(1).getItems());
            Assertions.assertEquals(orderList.get(2).getItems(), orderList.get(2).getItems());
            Assertions.assertEquals(orderList.get(0).getOrderItemWithProducts(), orderList.get(0).getOrderItemWithProducts());
            Assertions.assertEquals(orderList.get(1).getOrderItemWithProducts(), orderList.get(1).getOrderItemWithProducts());
            Assertions.assertEquals(orderList.get(2).getOrderItemWithProducts(), orderList.get(2).getOrderItemWithProducts());
        }
    }
}
