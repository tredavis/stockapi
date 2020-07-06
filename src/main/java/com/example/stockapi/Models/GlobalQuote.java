package com.example.stockapi.Models;

import com.example.stockapi.Models.SubModels.EMA;
import com.example.stockapi.Models.SubModels.MACD;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GlobalQuote {

    @JsonProperty("Global Quote")
    public Quote globalQuote = new Quote();

    public List<EMA> emaList = new ArrayList<>();

    public List<MACD> macdList = new ArrayList<>();

    public GlobalQuote() {

    }

}