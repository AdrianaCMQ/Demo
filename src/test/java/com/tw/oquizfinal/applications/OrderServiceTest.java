package com.tw.oquizfinal.applications;

import com.tw.oquizfinal.adapters.dto.OrderItemWithProduct;
import com.tw.oquizfinal.applications.coupon.DiscountCalculator;
import com.tw.oquizfinal.domain.order.Order;
import com.tw.oquizfinal.domain.order.OrderRepository;
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
    }

    @Test
    void should_return_order_when_save_it() {
        when(discountCalculator.getTotalPrice(order, List.of(orderItem))).thenReturn(PRICE);
        when(orderRepository.save(order)).thenReturn(order);

        Order savedOrder = orderService.save(order);

        assertEquals(order.getOrderId(),savedOrder.getOrderId());
        assertEquals(order.getAddressee(),savedOrder.getAddressee());
        assertEquals(order.getAddress(),savedOrder.getAddress());
        assertEquals(order.getMobile(),savedOrder.getMobile());
        assertEquals(order.getTotalPrice(),savedOrder.getTotalPrice());
        assertEquals(order.getCreatedAt(),savedOrder.getCreatedAt());
        assertEquals(List.of(orderItem), savedOrder.getItems());
    }

    @Test
    void should_return_items_with_product_info() {
        when(client.getProductDetail(1L)).thenReturn(Optional.of(product));

        List<OrderItemWithProduct> orderItemsWithProduct = orderService.getOrderItemsWithProduct(List.of(orderItem));

        assertEquals(1, orderItemsWithProduct.size());
        assertEquals(orderItem.getItemId(), orderItemsWithProduct.get(0).getItemId());
        assertEquals(orderItem.getProductId(), orderItemsWithProduct.get(0).getProductId());
        assertEquals(orderItem.getQuantity(), orderItemsWithProduct.get(0).getQuantity());
        assertEquals(product.getTitle(), orderItemsWithProduct.get(0).getTitle());
        assertEquals(product.getPrice(), orderItemsWithProduct.get(0).getPrice());
        assertEquals(product.getCategory(), orderItemsWithProduct.get(0).getCategory());
    }
}
