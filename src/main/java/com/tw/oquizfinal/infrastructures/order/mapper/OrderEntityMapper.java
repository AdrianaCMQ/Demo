package com.tw.oquizfinal.infrastructures.order.mapper;

import com.tw.oquizfinal.domain.order.Order;
import com.tw.oquizfinal.infrastructures.order.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderEntityMapper {
    OrderEntityMapper MAPPER = Mappers.getMapper(OrderEntityMapper.class);

    @Mapping(target = "items", source = "order.items")
    OrderEntity toEntity(Order order);

    Order toModel(OrderEntity entity);

    List<Order> toModel(List<OrderEntity> orderEntities);
}
