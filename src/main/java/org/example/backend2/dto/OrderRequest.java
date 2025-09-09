package org.example.backend2.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    @NotEmpty(message = "Full name is mandatory")
    @NotBlank(message = "Full name is mandatory")
    private String fullName;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Address is required")
    private String address;
    @Pattern(regexp = "\\d{5}", message = "Postal code must be 5 digits")
    private String postalCode;
    @NotBlank(message = "District is required")
    private String district;
    @Pattern(regexp = "[0-9\\-()\\s]+", message = "Invalid phone number format")
    private String mobileNumber;
    private boolean newsletter;
    private List<ProductDTO> products = new ArrayList<>();
    
}
