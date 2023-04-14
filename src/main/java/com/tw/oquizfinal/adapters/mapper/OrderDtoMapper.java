package com.tw.oquizfinal.adapters.mapper;

import com.tw.oquizfinal.adapters.dto.OrderItemWithProduct;
import com.tw.oquizfinal.adapters.dto.request.OrderRequest;
import com.tw.oquizfinal.adapters.dto.responses.OrderResponse;
import com.tw.oquizfinal.domain.order.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderDtoMapper {
    OrderDtoMapper MAPPER = Mappers.getMapper(OrderDtoMapper.class);

    @Mapping(target = "createdAt", source = "createdAt")
    Order toModel(OrderRequest orderRequest, Instant createdAt);

    @Mapping(target = "orderItems", source = "itemWithProductList")
    OrderResponse toResponse(Order order, List<OrderItemWithProduct> itemWithProductList);
}

