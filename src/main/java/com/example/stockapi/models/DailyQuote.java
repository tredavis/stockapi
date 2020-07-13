package com.example.stockapi.models;


import com.example.stockapi.entity.DailyAnalysis;
import com.example.stockapi.utility.HoldRating;
import com.example.stockapi.utility.Rating;
import com.example.stockapi.utility.Trend;

public class DailyQuote {

    public String ticker;
    public Rating rating;
    public String message;
    public Trend trend;

    public DailyQuote(){
        this.ticker = "";
        this.rating = null;
        this.message = "";
        this.trend = Trend.NUETRAL;
    }
}
