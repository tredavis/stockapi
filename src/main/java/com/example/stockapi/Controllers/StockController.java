package com.example.stockapi.Controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import com.example.stockapi.Models.GlobalQuote;
import com.example.stockapi.Models.StockSearches;
import com.example.stockapi.Models.Tickers;
import com.example.stockapi.Models.SubModels.EMA;
import com.example.stockapi.Models.SubModels.MACD;
import com.example.stockapi.Utilities.Extractor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("stock")
public class StockController {

    RestTemplate restTemplate = new RestTemplate();

    // Search for quotes.
    @GetMapping
    @RequestMapping("/search-ticker/{id}")
    public List<Tickers> searchTicker(@PathVariable final String id) {
        List<Tickers> searchList = new ArrayList<>();

        try{
            final StockSearches response = restTemplate.getForObject(String.format(
                    "https://www.alphavantage.co/query?function=SYMBOL_SEARCH&keywords=%s&apikey=VTPCO5SG7C08XOT5", id),
                    StockSearches.class);
            searchList = response.GrabMatches();

        } catch(Exception ex) {

        }

        return searchList;
    }

    // Grab stock quote.
    @GetMapping
    @RequestMapping("/grab-quote/{symbol}")
    public GlobalQuote getQuote(@PathVariable final String symbol) {

        final GlobalQuote quote = restTemplate.getForObject(String.format(
                "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=%s&apikey=VTPCO5SG7C08XOT5", symbol),
                GlobalQuote.class);

        return quote;
    }

    // Route to grab ema
    @RequestMapping("/grab-ema/{symbol}")
    public List<EMA> grabEma(@PathVariable final String symbol) {
        return Extractor.ExtractEMA(restTemplate, symbol);
    }

    // Route to grab macd information
    @RequestMapping("/grab-macd/{symbol}")
    public List<MACD> grabMacd(@PathVariable final String symbol) {

        return Extractor.ExtractMACD(restTemplate, symbol);
    }

    // Route to grab information about the sector
    @RequestMapping("/grab-sector")
    public String grabSector() {
        return "Route to grab sector";
    }

}