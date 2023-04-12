package com.tw.oquizfinal.infrastructure.order;

import com.tw.oquizfinal.infrastructure.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaOrderRepository extends JpaRepository<OrderEntity, Long> {
}
