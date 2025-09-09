package org.example.backend2.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
public class OrderRequest {
    private String fullName;
    private String email;
    private String address;
    private String postalCode;
    private String district;
    private String mobileNumber;
    private boolean newsletter;
    private List<ProductDTO> products = new ArrayList<>();
    
}
