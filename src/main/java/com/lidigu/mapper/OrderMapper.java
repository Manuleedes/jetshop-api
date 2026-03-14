package com.lidigu.mapper;

import com.lidigu.dto.OrderDTO;
import com.lidigu.dto.OrderItemDTO;
import com.lidigu.entity.Order;
import com.lidigu.entity.OrderItem;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderDTO toDto(Order order, List<OrderItem> items) {
        if (order == null)
            return null;
        OrderDTO dto = new OrderDTO();

        dto.setOrderId(order.getOrderId());
        dto.setOrderNumber(order.getOrderId());
        dto.setUserId(order.getUserId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setOrderStatus(order.getStatus());
        dto.setPaymentStatus(order.getPaymentStatus());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setCreatedAt(order.getCreatedAt() != null ? order.getCreatedAt().toString() : null);

        if (items != null) {
            dto.setItems(items.stream().map(this::toDto).collect(Collectors.toList()));
        }
        return dto;
    }

    public OrderItemDTO toDto(OrderItem item) {
        if (item == null)
            return null;
        OrderItemDTO dto = new OrderItemDTO();
        dto.setOrderItemId(String.valueOf(item.getOrderDetailsId()));
        dto.setProductId(item.getProductId());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        dto.setTotalPrice(item.getTotalPrice());
        return dto;
    }
}
