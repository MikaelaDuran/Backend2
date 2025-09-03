package org.example.backend2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleUpdate {
    @NotEmpty(message = "Username is mandatory")
    @NotBlank(message = "Username is mandatory")
    private String username;
    @NotEmpty(message = "Role is mandatory")
    @NotBlank(message = "Role is mandatory")
    private String role;
    private boolean shouldAssign;
}
