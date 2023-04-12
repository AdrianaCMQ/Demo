package com.tw.oquizfinal.infrastructure.order;

import com.tw.oquizfinal.domain.order.Order;
import com.tw.oquizfinal.domain.order.OrderRepository;
import com.tw.oquizfinal.infrastructure.order.entity.OrderEntity;
import com.tw.oquizfinal.infrastructure.order.mapper.OrderEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class OrderRepositoryProvider implements OrderRepository {

    private JpaOrderRepository jpaOrderRepository;

    @Override
    public Order saveOrder(Order order) {
        OrderEntity save = jpaOrderRepository.save(OrderEntityMapper.MAPPER.toEntity(order));

        return OrderEntityMapper.MAPPER.toModel(save);
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {
        Page<OrderEntity> orderEntities = jpaOrderRepository.findAll(pageable);
        List<Order> orders = OrderEntityMapper.MAPPER.toModel(orderEntities);

        return new PageImpl<>(orders, orderEntities.getPageable(), orderEntities.getTotalElements());
    }
}
