package com.tw.oquizfinal.applications;

import com.tw.oquizfinal.applications.coupon.DiscountCalculator;
import com.tw.oquizfinal.domain.order.Order;
import com.tw.oquizfinal.domain.order.OrderRepository;
import com.tw.oquizfinal.domain.orderItem.OrderItem;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final DiscountCalculator discountCalculator;

    @Transactional
    public Order save(Order order, List<OrderItem> orderItems) {
        order.setTotalPrice(discountCalculator.getTotalPrice(order, orderItems));

        return orderRepository.save(order);
    }
}
