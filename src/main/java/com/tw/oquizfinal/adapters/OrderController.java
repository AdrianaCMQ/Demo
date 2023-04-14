package com.tw.oquizfinal.adapters;

import com.tw.oquizfinal.adapters.dto.OrderItemWithProduct;
import com.tw.oquizfinal.adapters.dto.request.OrderRequest;
import com.tw.oquizfinal.adapters.dto.responses.OrderResponse;
import com.tw.oquizfinal.adapters.mapper.OrderDtoMapper;
import com.tw.oquizfinal.applications.OrderService;
import com.tw.oquizfinal.domain.order.Order;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;

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
}
