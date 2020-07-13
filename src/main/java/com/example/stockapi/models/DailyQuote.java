package com.example.stockapi.models;


import com.example.stockapi.entity.DailyAnalysis;
import com.example.stockapi.utility.HoldRating;
import com.example.stockapi.utility.Rating;

public class DailyQuote {

    public String ticker;
    public Rating rating;
    public String message;

    public DailyQuote(){
        this.ticker = null;
        this.rating = null;
        this.message = null;
    }
}
