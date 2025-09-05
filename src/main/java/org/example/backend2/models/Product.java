package org.example.backend2.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String title;
    public double price;
    @Column(length = 1000)
    public String description;
    public String category;
    public String image;

    @Embedded
    public Rating rating;
}
