package com.example.stockapi.models.subModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MACD {

    @JsonProperty("day")
    private String day = "";

    @JsonProperty("macd")
    private String macdValue = "";
    
    @JsonProperty("macdHist")
    public String macdHist = "";

    @JsonProperty("macdSignal")
    private String macdSignal = "";

    public MACD (String day, String macdValue, String macdHist, String macdSignal) {
        this.day = day;
        this.macdValue = macdValue;
        this.macdHist = macdHist;
        this.macdSignal = macdSignal;
    }

}