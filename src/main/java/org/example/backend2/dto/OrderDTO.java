package org.example.backend2.dto;

import lombok.*;

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
    private List<OrderItemDTO> orderItems;
    private Instant createdAt;
    private Instant lastUpdatedAt;
}
