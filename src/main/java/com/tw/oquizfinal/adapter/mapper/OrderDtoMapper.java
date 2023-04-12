package com.tw.oquizfinal.adapter.mapper;

import com.tw.oquizfinal.adapter.dto.OrderItemWithProduct;
import com.tw.oquizfinal.adapter.dto.requests.OrderRequest;
import com.tw.oquizfinal.adapter.dto.responses.OrderResponse;
import com.tw.oquizfinal.adapter.dto.responses.PageResponse;
import com.tw.oquizfinal.domain.order.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderDtoMapper {
    OrderDtoMapper MAPPER = Mappers.getMapper(OrderDtoMapper.class);

    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "totalPrice", source = "totalPrice")
    Order toEntity(OrderRequest orderRequest, Instant createdAt, BigDecimal totalPrice);

    @Mapping(target = "createdAt", source = "now")
    Order toModel(OrderRequest orderRequest, Instant now);

    @Mapping(target = "orderItems", source = "itemWithProductList")
    OrderResponse toResponse(Order order, List<OrderItemWithProduct> itemWithProductList);

    default PageRequest buildPageRequest(Integer page, Integer size, String sortBy, String orderBy) {
        Sort.Direction direction = "desc".equals(orderBy) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return PageRequest.of(page - 1, size, direction, sortBy);
    }

    @Named("intPlusOne")
    default Integer intPlusOne(Integer result) {
        return result + 1;
    }

    @Mappings({
            @Mapping(target = "current", source = "number", qualifiedByName = "intPlusOne"),
            @Mapping(target = "total", source = "totalPages")
    })
    PageResponse toPageResponse(Page<Order> orders);

    @Mapping(target = "orderItems", source = "itemWithProductList")
    OrderResponse toOrderResponse(Order order, List<OrderItemWithProduct> itemWithProductList);
}
