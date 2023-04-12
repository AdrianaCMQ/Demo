package com.tw.oquizfinal.applications;

import com.tw.oquizfinal.applications.coupon.DiscountCalculator;
import com.tw.oquizfinal.applications.exceptions.OrderItemIsEmptyException;
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

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final DiscountCalculator discountCalculator;
    private final ProductServiceClient client;

    @Transactional
    public Order createOrder(Order order, List<OrderItem> orderItems) {
        order.setTotalPrice(discountCalculator.getTotalPrice(order, orderItems));

        return orderRepository.saveOrder(order);
    }

    public Page<Order> getAllOrders(PageRequest page) {
        Page<Order> orders = orderRepository.findAll(page);

        orders.stream().forEach(order -> {
            if (order.getItems().isEmpty()) {
                throw new OrderItemIsEmptyException(order.getOrderId());
            }
        });

        return orders;
    }

    public Product findProductById(Long productId) {

        Optional<Product> productDetail = client.getProductDetail(productId);
        return productDetail.orElseThrow(() -> new ProductNotExistException(productId));
    }
}