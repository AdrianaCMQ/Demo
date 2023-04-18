package com.tw.oquizfinal.adapters;

import com.tw.oquizfinal.adapters.dto.OrderItemWithProduct;
import com.tw.oquizfinal.adapters.dto.request.OrderRequest;
import com.tw.oquizfinal.adapters.dto.responses.OrderItemResponse;
import com.tw.oquizfinal.adapters.dto.responses.OrderResponse;
import com.tw.oquizfinal.adapters.dto.responses.PageResponse;
import com.tw.oquizfinal.adapters.dto.responses.TotalOrdersResponse;
import com.tw.oquizfinal.adapters.mapper.OrderDtoMapper;
import com.tw.oquizfinal.applications.OrderService;
import com.tw.oquizfinal.domain.order.Order;
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
import java.util.stream.Stream;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(@RequestBody @Valid OrderRequest orderRequest){
        Order order = orderService.save(OrderDtoMapper.MAPPER.toModel(orderRequest, Instant.now()));
        List<OrderItemWithProduct> itemsWithProduct = orderService.getOrderItemsWithProduct(order.getItems());

        return OrderDtoMapper.MAPPER.toResponse(order, itemsWithProduct);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public TotalOrdersResponse getAllByPagination(@RequestParam(required = false) Integer page,
                                                  @RequestParam(required = false) Integer size,
                                                  @RequestParam(required = false, defaultValue = "desc") String orderBy,
                                                  @RequestParam(required = false, defaultValue = "createdAt") String sortBy)
    {
        if (page == null || size == null) {
            List<Order> orders = orderService.getOrders();
            List<OrderResponse> orderResponses = getOrderResponses(orders.stream());
            OrderItemResponse orderItemResponse = OrderItemResponse.buildBy(orderResponses);
            PageResponse pageResponse = new PageResponse(1, 1, 1);

            return new TotalOrdersResponse(pageResponse, orderItemResponse);
        }
        PageRequest pageRequest = OrderDtoMapper.MAPPER.buildPageRequest(page, size, orderBy, sortBy);
        Page<Order> orders = orderService.getOrdersByPage(pageRequest);
        List<OrderResponse> orderResponses = getOrderResponses(orders.stream());
        OrderItemResponse orderItemResponse = OrderItemResponse.buildBy(orderResponses, sortBy, orderBy);
        PageResponse pageResponse = OrderDtoMapper.MAPPER.toPageResponse(orders);

        return new TotalOrdersResponse(pageResponse, orderItemResponse);
    }

    @NotNull
    private List<OrderResponse> getOrderResponses(Stream<Order> orders) {

        return orders.map(order -> {
            List<OrderItemWithProduct> orderItemsWithProduct = orderService.getOrderItemsWithProduct(order.getItems());
            return OrderDtoMapper.MAPPER.toResponse(order, orderItemsWithProduct);
        }).collect(Collectors.toList());
    }
}
