package org.example.backend2.mapper;

import org.example.backend2.dto.OrderItemDTO;
import org.example.backend2.models.OrderItem;
import java.util.List;
import java.util.stream.Collectors;

public class OrderItemMapper {

    public static OrderItemDTO toDTO(OrderItem orderItem) {
        return OrderItemDTO.builder()
                .productId(orderItem.getProduct().getId())
                .productName(orderItem.getProduct().getTitle())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .build();
    }

    public static List<OrderItemDTO> multipleToDTO(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderItemMapper::toDTO)
                .collect(Collectors.toList());
    }
}
