package com.tw.oquizfinal.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.oquizfinal.adapters.dto.OrderItemWithProduct;
import com.tw.oquizfinal.adapters.dto.request.OrderRequest;
import com.tw.oquizfinal.applications.OrderService;
import com.tw.oquizfinal.domain.order.Order;
import com.tw.oquizfinal.domain.orderItem.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
public class OrderControllerTest {
    @MockBean
    private OrderService orderService;
    @Autowired
    private MockMvc mockMvc;

    public static final String TEST_ADDRESSEE = "test addressee";
    public static final String TEST_ADDRESS = "test address";
    public static final String MOBILE = "12345678987";
    public static final int QUANTITY = 10;
    public static final BigDecimal PRICE = BigDecimal.TEN;
    public static final String PRODUCT_TITLE = "product title";
    public static final String CATEGORY = "test category";

    private final OrderItem orderItem = new OrderItem();
    private final Order order = new Order();
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

        orderItemWithProduct.setItemId(1L);
        orderItemWithProduct.setProductId(1L);
        orderItemWithProduct.setTitle(PRODUCT_TITLE);
        orderItemWithProduct.setPrice(PRICE);
        orderItemWithProduct.setCategory(CATEGORY);
        orderItemWithProduct.setQuantity(QUANTITY);
    }

    @Test
    void should_return_success_when_save_order() throws Exception {
        OrderRequest orderRequest = new OrderRequest(TEST_ADDRESSEE, TEST_ADDRESS, MOBILE, 1L, List.of(orderItem));
        when(orderService.save(any())).thenReturn(order);
        when(orderService.getOrderItemsWithProduct(List.of(orderItem))).thenReturn(List.of(orderItemWithProduct));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(orderRequest))
                )
                .andExpect(status().isCreated());
    }
}
