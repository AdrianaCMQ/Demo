package com.tw.oquizfinal.applications;

import com.tw.oquizfinal.adapters.dto.OrderItemWithProduct;
import com.tw.oquizfinal.applications.coupon.DiscountCalculator;
import com.tw.oquizfinal.applications.exceptions.ProductNotExistException;
import com.tw.oquizfinal.domain.order.Order;
import com.tw.oquizfinal.domain.order.OrderRepository;
import com.tw.oquizfinal.domain.orderItem.OrderItem;
import com.tw.oquizfinal.domain.product.Product;
import com.tw.oquizfinal.domain.product.ProductServiceClient;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final DiscountCalculator discountCalculator;
    private final ProductServiceClient client;

    @Transactional
    public Order save(Order order) {
        order.setTotalPrice(discountCalculator.getTotalPrice(order, order.getItems()));

        return orderRepository.save(order);
    }

    public List<OrderItemWithProduct> getOrderItemsWithProduct(List<OrderItem> items) {
        return items.stream().map(orderItem -> {
            Product product = client.getProductDetail(orderItem.getProductId())
                    .orElseThrow(() -> new ProductNotExistException(orderItem.getProductId()));

            return new OrderItemWithProduct(
                    orderItem.getItemId(), orderItem.getProductId(),
                    product.getTitle(), product.getPrice(),
                    product.getCategory(), orderItem.getQuantity());
        }).collect(Collectors.toList());
    }

    public List<Order> getOrders() {
        return Collections.emptyList();
    }

    public Page<Order> getOrdersByPage(PageRequest pageRequest) {
        return Page.empty();
    }
}
