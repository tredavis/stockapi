package com.example.stockapi.controller;


import com.example.stockapi.contant.ApplicationConstants;
import com.example.stockapi.dao.GlobalQuoteDao;
import com.example.stockapi.dao.SymbolDao;
import com.example.stockapi.entity.Symbol;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import com.example.stockapi.entity.GlobalQuote;
import com.example.stockapi.models.StockSearches;
import com.example.stockapi.models.Tickers;
import com.example.stockapi.utility.Indicators;

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

    @Autowired
    private GlobalQuoteDao globalQuoteDao;

    @Autowired
    private SymbolDao symbolDao;

    RestTemplate restTemplate = new RestTemplate();

    @RequestMapping("/")
    public String init() {
        CompletableFuture<List<Symbol>> completableFuture = CompletableFuture.supplyAsync(new Supplier<List<Symbol>>() {
            @Override
            public List<Symbol> get() {
                log.info("Loading in the info for the symbols: " + new Timestamp(System.currentTimeMillis()));
                List<Symbol> symbols = symbolDao.grabUniqueSymbolsFromDb();

                for (int i = 0; i < symbols.size(); i++) {
                    log.info("Symbol name - " + symbols.get(i));
                }
                log.info("Finished loading in the info for the ticker symbols: " + new Timestamp(System.currentTimeMillis()));
                return symbols;
            }
        });
        return "Route to grab sector";
    }


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

        // check to see if there is already an entry in the database for the quote today/
        // if so then return that so we don't waste an external call
        try {
            quote = globalQuoteDao.getQuoteByTickerAndDate(symbol);
            if(quote.id != null) {
                return quote;
            }
        } catch (Exception ex) {
            log.error("There was an exception retrieving the information from the database: " + ex);
        }

        // if we don't have an entry grab the information externally, save and return this data.
        try{
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();

            // retrieve stock quote information
            quote = restTemplate.getForObject(String.format(applicationConstants.AV_GLOBAL_QUOTE, symbol), GlobalQuote.class);
            quote.tickerSymbol = symbol;
            quote.emaList = indicators.ExtractDailyEMA20(restTemplate, symbol);
            quote.macdList = indicators.ExtractDailyMACD(restTemplate, symbol);
            quote.recordedDate = dtf.format(now);

            try {
                // try to save to the mongo db
                globalQuoteDao.saveQuote(quote);

            } catch (Exception ex) {
                log.error("There was an error saving to the database: " + ex);
            }

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