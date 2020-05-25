package com.example.stockapi.Models.SubModels;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EMA {

    @JsonProperty("day")
    private String Day = "";

    @JsonProperty("ema")
    private String Value = "";

    public EMA (String day, String ema_value) {
        Day = day;
        Value = ema_value;
    }

}