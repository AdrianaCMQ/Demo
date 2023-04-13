package com.tw.oquizfinal.infrastructures.order;

import com.tw.oquizfinal.domain.order.Order;
import com.tw.oquizfinal.domain.order.OrderRepository;
import com.tw.oquizfinal.infrastructures.order.entity.OrderEntity;
import com.tw.oquizfinal.infrastructures.order.mapper.OrderEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderRepositoryProvider implements OrderRepository {
    private JpaOrderRepository jpaOrderRepository;


    @Override
    public Order save(Order order) {
        OrderEntity entity = jpaOrderRepository.save(OrderEntityMapper.MAPPER.toEntity(order));

        return OrderEntityMapper.MAPPER.toModel(entity);
    }
}
