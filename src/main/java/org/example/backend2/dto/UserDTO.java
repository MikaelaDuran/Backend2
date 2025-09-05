package org.example.backend2.dto;

import jakarta.persistence.*;
import lombok.*;
import org.example.backend2.models.Role;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {


    private Long id;
    private String username;
    private Set<String> roles = new HashSet<>();

}

