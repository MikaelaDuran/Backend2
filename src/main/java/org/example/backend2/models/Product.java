package org.example.backend2.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Product {
    @Id
    private Long id; 
    private String title;
    private double price;
    @Column(length = 1000)
    private String description;
    private String category;
    private String image;

    @Embedded
    private Rating rating;
}
