package com.tw.oquizfinal.infrastructure.order.mapper;

import com.tw.oquizfinal.domain.order.Order;
import com.tw.oquizfinal.infrastructure.order.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderEntityMapper {
    OrderEntityMapper MAPPER = Mappers.getMapper(OrderEntityMapper.class);

    @Mapping(target = "items", source = "order.items")
    OrderEntity toEntity(Order order);

    List<Order> toModel(Page<OrderEntity> orderEntities);

    Order toModel(OrderEntity orderEntity);
}
