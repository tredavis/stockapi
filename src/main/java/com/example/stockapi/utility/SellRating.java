package com.example.stockapi.utility;

public class SellRating extends Rating {

    private boolean isActive = false;

    public SellRating(boolean active) {
        super("Sell", active);
        this.isActive = active;
    }

}
