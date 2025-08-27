package org.example.backend2.models;

import jakarta.persistence.Embeddable;

@Embeddable
public class Rating {
    public double rate;
    public int count;

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
