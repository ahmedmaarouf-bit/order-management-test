package com.example.ordermanagement.mapper;

import com.example.ordermanagement.dto.OrderDTO;
import com.example.ordermanagement.dto.OrderItemDTO;
import com.example.ordermanagement.model.Order;
import com.example.ordermanagement.model.OrderItem;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerName", source = "customer.email")
    @Mapping(target = "customerEmail", source = "customer.name")
    @Mapping(target = "items", source = "orderItems")
    @Mapping(target = "status", expression = "java(order.getStatus().name())")
    OrderDTO toDTO(Order order);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "subtotal", expression = "java(item.getUnitPrice().multiply(java.math.BigDecimal.valueOf(item.getQuantity())))")
    OrderItemDTO toOrderItemDTO(OrderItem item);

    List<OrderItemDTO> toOrderItemDTOList(List<OrderItem> items);
}