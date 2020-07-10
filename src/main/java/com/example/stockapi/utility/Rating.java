package com.example.stockapi.utility;

import com.fasterxml.jackson.databind.ser.Serializers;

public class Rating {

    private String ratingValue;
    private boolean activeRating;

    public Rating(String ratingValue, boolean activeRating) {
        this.ratingValue = ratingValue;
        this.activeRating = activeRating;
    }
}
