package com.example.stockapi.Models.SubModels;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MACD {

    @JsonProperty("day")
    private String Day = "";

    @JsonProperty("macd")
    private String MacdValue = "";
    
    @JsonProperty("macdHist")
    private String MacdHist = "";

    @JsonProperty("macdSignal")
    private String MacdSignal = "";

    public MACD (String day, String macd_value, String macd_hist, String macd_signal) {
        Day = day;
        MacdValue = macd_value;
        MacdHist = macd_hist;
        MacdSignal = macd_signal;
    }

}