package com.example.stockapi.utility;

public class BuyRating extends Rating {

    private boolean isActive = false;

    public BuyRating(boolean active) {
        super("Buy", active);
        this.isActive = active;
    }
}
