package com.tw.oquizfinal.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.oquizfinal.adapter.dto.requests.OrderRequest;
import com.tw.oquizfinal.adapter.mapper.OrderDtoMapper;
import com.tw.oquizfinal.applications.OrderService;
import com.tw.oquizfinal.domain.order.Order;
import com.tw.oquizfinal.domain.orderItem.OrderItem;
import com.tw.oquizfinal.domain.product.Product;
import com.tw.oquizfinal.support.exceptions.ApiError;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.tw.oquizfinal.adapter.mapper.OrderDtoMapper.MAPPER;
import static java.util.Arrays.asList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    @Test
    void shouldReturnSuccessWhenSaveOrder() throws Exception {
        OrderItem orderItem = new OrderItem(1L, 1L, 10);
        OrderRequest orderRequest = new OrderRequest("addressee", "address", "11111111111", 1L, List.of(orderItem));
        Order order = OrderDtoMapper.MAPPER.toEntity(orderRequest, Instant.now(), BigDecimal.ONE);
        order.setItems(List.of(orderItem));
        Product product = new Product(1L, "title1", BigDecimal.ONE, "category1");
        when(orderService.createOrder(any(), anyList())).thenReturn(order);
        when(orderService.findProductById(orderItem.getProductId())).thenReturn(product);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(orderRequest))
                )
                .andExpect(status().isCreated());

    }

    @Test
    void parameterisedShouldReturnBadRequestWhenRequestHaveBlankField() throws Exception {
        Map<String, List<String>> args = new HashMap<>();
        args.put(null, List.of("addressee cannot be blank"));
        for (Map.Entry<String, List<String>> entity : args.entrySet()) {
            shouldReturnBadRequestWhenRequestHaveBlankField(entity.getKey(), entity.getValue());
        }
    }

    void shouldReturnBadRequestWhenRequestHaveBlankField(String addressee, List<String> errors) throws Exception {
        OrderItem orderItem = new OrderItem(1L, 1L, 10);
        OrderRequest orderRequest = new OrderRequest(addressee, "", "", 1L, List.of(orderItem));

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(orderRequest))
                )
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        ApiError apiError = new ObjectMapper().readValue(response.getContentAsString(), ApiError.class);
        List<String> responseMessage = Stream.of(apiError.getMessage().split(";")).collect(Collectors.toList());

        errors.forEach(message -> assertTrue(responseMessage.contains(message)));
    }

    @Test
    void shouldReturnBadRequestWhenRequestHaveEmptyField() throws Exception {
        OrderRequest orderRequest = new OrderRequest("addressee", "address", "15771534215", 1L, Collections.emptyList());
        String message = "orderItems cannot be empty";

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(orderRequest))
                )
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        ApiError apiError = new ObjectMapper().readValue(response.getContentAsString(), ApiError.class);
        List<String> responseMessage = Stream.of(apiError.getMessage().split(";")).collect(Collectors.toList());

        assertTrue(responseMessage.contains(message));
    }

    @Test
    void shouldReturnBadRequestWhenRequestHaveNullField() throws Exception {
        OrderItem orderItem = new OrderItem(1L,1L, 10);
        OrderRequest orderRequest = new OrderRequest("addressee", "address", "15771534215", null, List.of(orderItem));
        String message = "must not be null";

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(orderRequest))
                )
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        ApiError apiError = new ObjectMapper().readValue(response.getContentAsString(), ApiError.class);
        List<String> responseMessage = Stream.of(apiError.getMessage().split(";")).collect(Collectors.toList());

        assertTrue(responseMessage.contains(message));
    }


    @Nested
    class getOrders {

        OrderItem orderItem1 = new OrderItem(1L, 1L, 10);
        OrderItem orderItem2 = new OrderItem(2L, 2L, 20);

        List<Order> orders = asList(
                Order.builder().orderId(1L).addressee("addressee").address("address").mobile("11111111111")
                        .totalPrice(BigDecimal.valueOf(108.88)).createdAt(Instant.now()).items(List.of(orderItem1, orderItem2)).build(),
                Order.builder().orderId(2L).addressee("addressee").address("address").mobile("11111111111")
                        .totalPrice(BigDecimal.valueOf(208.88)).createdAt(Instant.now().plusSeconds(1)).items(List.of(orderItem1, orderItem2)).build(),
                Order.builder().orderId(3L).addressee("addressee").address("address").mobile("11111111111")
                        .totalPrice(BigDecimal.valueOf(308.88)).createdAt(Instant.now().plusSeconds(2)).items(List.of(orderItem1, orderItem2)).build()
        );

        @Test
        void shouldGetEmptyWhenHasNoOrder() throws Exception {
            PageRequest pageRequest = MAPPER.buildPageRequest(1, 10, "updatedAt", "desc");

            when(orderService.getAllOrders(any())).thenReturn(new PageImpl<>(Collections.emptyList(), pageRequest, 0));

            mockMvc.perform(MockMvcRequestBuilders
                            .get("/orders?page=1&size=10")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$.page").exists())
                    .andExpect(jsonPath("$.page.current").value(1))
                    .andExpect(jsonPath("$.page.size").value(10))
                    .andExpect(jsonPath("$.page.total").value(0))
                    .andExpect(jsonPath("$.items").exists())
                    .andExpect(jsonPath("$.items.total").value(0))
                    .andExpect(jsonPath("$.items.sortBy").value("createdAt"))
                    .andExpect(jsonPath("$.items.orderBy").value("desc"))
                    .andExpect(jsonPath("$.items.data").isEmpty());
        }

        @Test
        void shouldGetTwoPagesAndDescWhenHasThreeDataAndOrderByIsDesc() throws Exception {
            PageRequest pageRequest = MAPPER.buildPageRequest(1, 2, "updatedAt", "desc");

            when(orderService.getAllOrders(any())).thenReturn(new PageImpl<>(orders, pageRequest, 6));

            Product product1 = new Product(1L, "title1", BigDecimal.ONE, "category1");
            Product product2 = new Product(2L, "title2", BigDecimal.ONE, "category2");
            when(orderService.findProductById(orderItem1.getProductId())).thenReturn(product1);
            when(orderService.findProductById(orderItem2.getProductId())).thenReturn(product2);

            mockMvc.perform(MockMvcRequestBuilders
                            .get("/orders?page=1&size=2&sortBy=createdAt&orderBy=desc")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$.page").exists())
                    .andExpect(jsonPath("$.page.current").value(1))
                    .andExpect(jsonPath("$.page.size").value(2))
                    .andExpect(jsonPath("$.page.total").value(3))
                    .andExpect(jsonPath("$.items").exists())
                    .andExpect(jsonPath("$.items.total").value(3))
                    .andExpect(jsonPath("$.items.sortBy").value("createdAt"))
                    .andExpect(jsonPath("$.items.orderBy").value("desc"))
                    .andExpect(jsonPath("$.items.data").exists())
                    .andExpect(jsonPath("$.items.data").isArray())
                    .andExpect(jsonPath("$.items.data", hasSize(3)));
        }
    }
}
