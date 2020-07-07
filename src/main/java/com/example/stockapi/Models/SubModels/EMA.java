package com.example.stockapi.Models.SubModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EMA {

    @JsonProperty("day")
    private String day = "";

    @JsonProperty("ema")
    private String value = "";

    public EMA (String day, String value) {
        this.day = day;
        this.value = value;
    }

}