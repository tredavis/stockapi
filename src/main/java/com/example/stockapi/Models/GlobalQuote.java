package com.example.stockapi.Models;

import com.example.stockapi.Models.SubModels.EMA;
import com.example.stockapi.Models.SubModels.MACD;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
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