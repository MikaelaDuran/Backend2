package org.example.backend2.dto;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private long userId;
    private String username;
    private Set<String> roles = new HashSet<>();
}
