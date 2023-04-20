package com.tw.oquizfinal.domain.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepository {
    Order save(Order order);

    Page<Order> findAllByPage(Pageable pageable);
}
