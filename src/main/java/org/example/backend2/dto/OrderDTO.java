package org.example.backend2.dto;

import lombok.*;
import org.example.backend2.models.OrderItem;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private long orderId;
    private UserDTO user;
    private List<OrderItem> orderItems;
    private Instant createdAt;
    private Instant lastUpdatedAt;
}
