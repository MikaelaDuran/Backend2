package org.example.backend2.mapper;

import lombok.RequiredArgsConstructor;
import org.example.backend2.dto.OrderDTO;
import org.example.backend2.models.ProductOrder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final UserMapper userMapper;
    private final OrderItemMapper orderItemMapper;

    public OrderDTO toDTO(ProductOrder productOrder) {
        return OrderDTO.builder()
                .orderId(productOrder.getId())
                .user(userMapper.toDTO(productOrder.getAppUser()))
                .orderItems(orderItemMapper.multipleToDTO(productOrder.getOrderItems()))
                .createdAt(productOrder.getCreatedAt())
                .lastUpdatedAt(productOrder.getLastUpdatedAt())
                .build();
    }

    public List<OrderDTO> multipleToDTO(List<ProductOrder> orders) {
        return orders.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
