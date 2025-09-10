package org.example.backend2.models;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Embeddable
public class Rating {
    private double rate;
    private int count;
}
