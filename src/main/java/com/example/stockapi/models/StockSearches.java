package com.example.stockapi.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StockSearches {
    
    @JsonProperty("bestMatches")
    private List<Tickers> bestMatches;
 
    public StockSearches() {
        bestMatches = new ArrayList<>();
    }
    
    @JsonProperty("bestMatches")
    public List<Tickers> GrabMatches(){
        return bestMatches;
    }
}