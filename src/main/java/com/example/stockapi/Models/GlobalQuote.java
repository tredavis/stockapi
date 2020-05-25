package com.example.stockapi.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GlobalQuote {

    @JsonProperty("Global Quote")
    private Quote globalQuote;

    public GlobalQuote() {

    }

    @JsonProperty("Global Quote")
    public Quote getQuote() {
        return globalQuote;
    }

}