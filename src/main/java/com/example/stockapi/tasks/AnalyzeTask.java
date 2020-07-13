package com.example.stockapi.tasks;

import com.example.stockapi.contant.ApplicationConstants;
import com.example.stockapi.dao.DailyAnalysisDao;
import com.example.stockapi.dao.GlobalQuoteDao;
import com.example.stockapi.dao.SymbolDao;
import com.example.stockapi.entity.GlobalQuote;
import com.example.stockapi.models.subModels.MACD;
import com.example.stockapi.utility.Indicators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimerTask;

/**
 *
 */
public class AnalyzeTask extends TimerTask {
    @Autowired
    private ApplicationConstants applicationConstants;

    @Autowired
    private Indicators indicators;

    @Autowired
    private GlobalQuoteDao globalQuoteDao;

    @Autowired
    private DailyAnalysisDao dailyAnalysisDao;

    RestTemplate restTemplate = new RestTemplate();

    public AnalyzeTask(GlobalQuoteDao globalQuoteDao, DailyAnalysisDao dailyAnalysisDao){
        this.globalQuoteDao = globalQuoteDao;
        this.dailyAnalysisDao = dailyAnalysisDao;
    }

    @Override
    public void run() {

        GlobalQuote quote = new GlobalQuote();
        // check to see if there is already an entry in the database for the quote today/
        // if so then return that so we don't waste an external call
        try {
            quote = globalQuoteDao.getQuoteByTickerAndDateFromDb(symbol);
            if(quote.id != null) {  }
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

        // perform analysis
        // we need a quote, ema and the macd values for the last 90 days
        if(quote.globalQuote != null && quote.emaList.size() > 0 && quote.macdList.size() > 0) {
            LocalDate today = LocalDate.parse(quote.recordedDate);
            int dayInCycle = 0;

            // analyze the MACD list first
            // walk backwards and first search for the lowest and highest points.
            // if you hit a value that is neither the highest or lowest point. Then we have a point of interest.
            //
            for (int m = 0; m < quote.macdList.size(); m++) {
                MACD current = quote.macdList.get(m);
                MACD previousDay = quote.macdList.get(m + 1);

                Double currentValue = current.macdHist;
                Double previousValue = previousDay.macdHist;

                // the current value is greater than zero than we are either in an upward trend, upward peak, or upward fall.
                // if we are in an upward trend we will see three (arbitrary) days of the previous values being lower or within decimals than the days before
                if (currentValue >= 0) {

                    // possible upward trend
                    if(currentValue >= previousValue) {

                    }
                    // possible downward trend
                    else if (currentValue <= previousValue) {

                    }

                } else {
                    // if the current value is less than zero than we are either in a downward trend, downward peak, or downward fall.
                    // possible upward trend
                    if(currentValue >= previousValue) {

                    }
                    // possible downward trend
                    else if (currentValue <= previousValue) {

                    }
                }

                // we need to break before we hit an index out of bounds exception
                if((m+1) == quote.macdList.size()){
                    break;
                }
            }
        }
    }
}
