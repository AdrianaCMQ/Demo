package com.tw.oquizfinal.infrastructure.order.entity;

import com.tw.oquizfinal.infrastructure.orderItem.entity.OrderItemEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private String addressee;
    private String address;
    private String mobile;
    private BigDecimal totalPrice;
    private Instant createdAt;
    private Long couponId;
    @OneToMany(targetEntity = OrderItemEntity.class, cascade = CascadeType.ALL)
    private List<OrderItemEntity> items;
}
