package com.example.stockapi.entity;

import com.example.stockapi.models.Quote;
import com.example.stockapi.models.subModels.EMA;
import com.example.stockapi.models.subModels.MACD;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GlobalQuote {

    @Id
    public String id;

    @JsonProperty("Global Quote")
    public Quote globalQuote = new Quote();

    public String tickerSymbol;

    public List<EMA> emaList = new ArrayList<>();

    public List<MACD> macdList = new ArrayList<>();

    public String recordedDate;

    public GlobalQuote() {

    }

}