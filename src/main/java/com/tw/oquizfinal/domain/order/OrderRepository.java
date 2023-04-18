package com.tw.oquizfinal.domain.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface OrderRepository {
    Order save(Order order);

    List<Order> findAll();

    Page<Order> findAllByPage(PageRequest pageRequest);
}
