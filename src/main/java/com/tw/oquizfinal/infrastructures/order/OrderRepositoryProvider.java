package com.tw.oquizfinal.infrastructures.order;

import com.tw.oquizfinal.domain.order.Order;
import com.tw.oquizfinal.domain.order.OrderRepository;
import com.tw.oquizfinal.infrastructures.order.entity.OrderEntity;
import com.tw.oquizfinal.infrastructures.order.mapper.OrderEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class OrderRepositoryProvider implements OrderRepository {
    private JpaOrderRepository jpaOrderRepository;


    @Override
    public Order save(Order order) {
        OrderEntity entity = jpaOrderRepository.save(OrderEntityMapper.MAPPER.toEntity(order));

        return OrderEntityMapper.MAPPER.toModel(entity);
    }

    @Override
    public List<Order> findAll() {
        return OrderEntityMapper.MAPPER.toModel(jpaOrderRepository.findAll());
    }

    @Override
    public Page<Order> findAllByPage(PageRequest pageRequest) {
        return Page.empty();
    }
}
