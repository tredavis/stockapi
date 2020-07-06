package com.example.stockapi.Controllers;


import com.example.stockapi.Constants.ApplicationConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import com.example.stockapi.Models.GlobalQuote;
import com.example.stockapi.Models.StockSearches;
import com.example.stockapi.Models.Tickers;
import com.example.stockapi.Models.SubModels.EMA;
import com.example.stockapi.Models.SubModels.MACD;
import com.example.stockapi.Utilities.Indicators;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RestController
@RequestMapping("stock")
public class StockController {

    @Autowired
    private ApplicationConstants applicationConstants;

    @Autowired
    private Indicators indicators;

    RestTemplate restTemplate = new RestTemplate();

    // Search for quotes.
    @GetMapping
    @RequestMapping("/search-ticker/{symbol}")
    public List<Tickers> searchTicker(@PathVariable final String symbol) {
        log.info("Grabbing Quote for ticker symbol: " + symbol);
        List<Tickers> searchList = new ArrayList<>();

        try{
            final StockSearches response = restTemplate.getForObject(String.format(applicationConstants.AV_SYMBOL_SEARCH , symbol), StockSearches.class);
            searchList = response.GrabMatches();

        } catch(Exception ex) {
            log.error("There was an exception grabbing the data searching for the ticket: " + ex.getMessage());
        }

        log.info("Completed grabbing Quote for ticket symbol: " + symbol);
        return searchList;
    }

    // Grab stock quote.
    @GetMapping
    @RequestMapping("/grab-quote/{symbol}")
    public GlobalQuote getQuote(@PathVariable final String symbol) {
        log.info("Grabbing Quote for ticker symbol: " + symbol);

        GlobalQuote quote = new GlobalQuote();
        try{
            quote = restTemplate.getForObject(String.format(applicationConstants.AV_GLOBAL_QUOTE, symbol), GlobalQuote.class);
            quote.emaList = indicators.ExtractDailyEMA10(restTemplate, symbol);
            quote.macdList = indicators.ExtractDailyMACD(restTemplate, symbol);

        } catch(Exception ex){
            log.error("There was an exception grabbing the quote for ticker: " + symbol + " Execption: " + ex.getMessage());
        }

        log.info("Completed grabbing Quote for ticket symbol: " + symbol);
        return quote;
    }

    // Route to grab information about the sector
    @RequestMapping("/grab-sector")
    public String grabSector() {
        return "Route to grab sector";
    }

}