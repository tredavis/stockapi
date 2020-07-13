package com.example.stockapi.utility;

public class HoldRating extends Rating {

    private boolean isActive = false;

    public HoldRating(boolean active) {
        super("Hold", active);
        this.isActive = active;
    }
}
