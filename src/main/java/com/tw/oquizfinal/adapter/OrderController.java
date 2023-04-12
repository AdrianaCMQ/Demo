package com.tw.oquizfinal.adapter;

import com.tw.oquizfinal.adapter.dto.OrderItemWithProduct;
import com.tw.oquizfinal.adapter.dto.requests.OrderRequest;
import com.tw.oquizfinal.adapter.dto.responses.OrderItemResponse;
import com.tw.oquizfinal.adapter.dto.responses.OrderResponse;
import com.tw.oquizfinal.adapter.dto.responses.PageResponse;
import com.tw.oquizfinal.adapter.dto.responses.TotalOrdersResponse;
import com.tw.oquizfinal.adapter.mapper.OrderDtoMapper;
import com.tw.oquizfinal.applications.OrderService;
import com.tw.oquizfinal.domain.order.Order;
import com.tw.oquizfinal.domain.orderItem.OrderItem;
import com.tw.oquizfinal.domain.product.Product;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(@RequestBody @Valid OrderRequest orderRequest){
        Order order = orderService.createOrder(OrderDtoMapper.MAPPER.toModel(orderRequest, Instant.now()),
                orderRequest.getItems());
        List<OrderItemWithProduct> itemWithProductList = getItemWithProductList(order.getItems());

        return OrderDtoMapper.MAPPER.toResponse(order, itemWithProductList);
    }

    @NotNull
    private List<OrderItemWithProduct> getItemWithProductList(List<OrderItem> orderItems) {
        return orderItems.stream().map(orderItem -> {
            Product product = orderService.findProductById(orderItem.getProductId());
            return new OrderItemWithProduct(
                    orderItem.getItemId(), orderItem.getProductId(),
                    product.getTitle(), product.getPrice(),
                    product.getCategory(), orderItem.getQuantity());
        }).collect(Collectors.toList());
    }

    @GetMapping
    @Operation(summary = "get all orders by pagination")
    @ResponseStatus(HttpStatus.OK)
    public TotalOrdersResponse getAllByPagination(@RequestParam(required = false) Integer page,
                                                  @RequestParam(required = false) Integer size,
                                                  @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
                                                  @RequestParam(required = false, defaultValue = "desc") String orderBy) {
        PageRequest pageRequest = OrderDtoMapper.MAPPER.buildPageRequest(page, size, sortBy, orderBy);

        Page<Order> orders = orderService.getAllOrders(pageRequest);

        List<OrderResponse> orderResponses = orders.stream().map(order -> OrderDtoMapper.MAPPER.toOrderResponse(order, getItemWithProductList(order.getItems()))).collect(Collectors.toList());
        OrderItemResponse orderItemResponse = OrderItemResponse.buildBy(orderResponses, sortBy, orderBy);
        PageResponse pageResponse = OrderDtoMapper.MAPPER.toPageResponse(orders);

        return new TotalOrdersResponse(pageResponse, orderItemResponse);
    }
}
