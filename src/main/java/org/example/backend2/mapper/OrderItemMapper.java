package org.example.backend2.mapper;

import org.example.backend2.dto.OrderItemDTO;
import org.example.backend2.models.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderItemMapper {

    public OrderItemDTO toDTO(OrderItem orderItem) {
        return OrderItemDTO.builder()
                .productId(orderItem.getProduct().getId())
                .productName(orderItem.getProduct().getTitle())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .build();
    }

    public List<OrderItemDTO> multipleToDTO(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
