package com.tw.oquizfinal.infrastructures.order;

import com.tw.oquizfinal.infrastructures.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaOrderRepository extends JpaRepository<OrderEntity, Long> {
}
