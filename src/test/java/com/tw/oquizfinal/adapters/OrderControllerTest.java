package com.tw.oquizfinal.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.oquizfinal.adapters.dto.OrderItemWithProduct;
import com.tw.oquizfinal.adapters.dto.request.OrderRequest;
import com.tw.oquizfinal.applications.OrderService;
import com.tw.oquizfinal.domain.order.Order;
import com.tw.oquizfinal.domain.orderItem.OrderItem;
import com.tw.oquizfinal.support.exceptions.ApiError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
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

    @Nested
    class saveOrder {
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

        @ParameterizedTest
        @MethodSource("provideEmptyOrderItemsAndErrors")
        void parameterised_should_return_bad_request_when_request_have_empty_field(List<OrderItem> orderItems, String errors) throws Exception {
            OrderRequest orderRequest = new OrderRequest("addressee", "address", "15771534215", 1L, orderItems);

            MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                            .post("/orders")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(orderRequest))
                    )
                    .andExpect(status().isBadRequest())
                    .andReturn()
                    .getResponse();

            ApiError apiError = new ObjectMapper().readValue(response.getContentAsString(), ApiError.class);
            List<String> responseMessage = Stream.of(apiError.getMessage().split(";")).toList();

            assertTrue(responseMessage.contains(errors));
        }

        static Stream<Arguments> provideEmptyOrderItemsAndErrors() {
            return Stream.of(
                    Arguments.of(Collections.emptyList(), "orderItems cannot be empty"),
                    Arguments.of(null, "orderItems cannot be empty")
            );
        }

        @ParameterizedTest
        @MethodSource("provideBlankFieldsAndErrors")
        void parameterised_should_return_bad_request_when_request_have_blank_field(String blankField, List<String> errors) throws Exception {
            OrderItem orderItem = new OrderItem(1L, 1L, 10);
            OrderRequest orderRequest = new OrderRequest(blankField, blankField, blankField, 1L, List.of(orderItem));

            MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                            .post("/orders")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(orderRequest))
                    )
                    .andExpect(status().isBadRequest())
                    .andReturn()
                    .getResponse();

            ApiError apiError = new ObjectMapper().readValue(response.getContentAsString(), ApiError.class);
            List<String> responseMessage = Stream.of(apiError.getMessage().split(";")).toList();

            errors.forEach(message -> assertTrue(responseMessage.contains(message)));
        }

        static Stream<Arguments> provideBlankFieldsAndErrors() {
            List<String> errorStringList = List.of("addressee cannot be blank", "address cannot be blank", "mobile cannot be blank");
            return Stream.of(
                    Arguments.of(null, errorStringList),
                    Arguments.of("", errorStringList),
                    Arguments.of("  ", errorStringList)
            );
        }
    }
}
