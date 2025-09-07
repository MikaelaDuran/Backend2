package org.example.backend2.mapper;

import org.example.backend2.dto.OrderDTO;
import org.example.backend2.dto.UserDTO;
import org.example.backend2.models.AppUser;
import org.example.backend2.models.ProductOrder;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderDTO toDTO(ProductOrder productOrder) {
        return OrderDTO.builder()
                .orderId(productOrder.getId())
                .user(UserMapper.toDTO(productOrder.getAppUser()))
                .orderItems(productOrder.getOrderItems())
                .createdAt(productOrder.getCreatedAt())
                .lastUpdatedAt(productOrder.getLastUpdatedAt())
                .build();
    }

    public static List<OrderDTO> multipleToDTO(List<ProductOrder> orders) {
        return orders.stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
    }
}
