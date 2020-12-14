package com.example.stockapi.tasks;

import com.example.stockapi.contant.ApplicationConstants;
import com.example.stockapi.dao.DailyAnalysisDao;
import com.example.stockapi.dao.GlobalQuoteDao;
import com.example.stockapi.dao.SymbolDao;
import com.example.stockapi.entity.DailyAnalysis;
import com.example.stockapi.entity.GlobalQuote;
import com.example.stockapi.models.DailyQuote;
import com.example.stockapi.models.subModels.MACD;
import com.example.stockapi.utility.*;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@Slf4j
public class AnalyzeTask extends TimerTask {

    @Autowired
    private Indicators indicators = new Indicators();

    @Autowired
    private GlobalQuoteDao globalQuoteDao;

    @Autowired
    private DailyAnalysisDao dailyAnalysisDao;

    RestTemplate restTemplate = new RestTemplate();

    private String date = "";

    public AnalyzeTask(GlobalQuoteDao globalQuoteDao, DailyAnalysisDao dailyAnalysisDao) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        this.date = dtf.format(now);
        this.globalQuoteDao = globalQuoteDao;
        this.dailyAnalysisDao = dailyAnalysisDao;
    }

    @Override
    public void run() {
        log.info("Running analyze task-- " + new Timestamp(System.currentTimeMillis()));

        // grab today's batch
        DailyAnalysis dailyAnalysis = this.grabTodayAnalysis();

        if (dailyAnalysis != null && dailyAnalysis.dailyQuotes.size() > 0) {

            for (int i = 0; i < dailyAnalysis.dailyQuotes.size(); i++) {

                // grab each quote, which contains the ticker symbol and perform analysis
                DailyQuote dailyQuote = dailyAnalysis.dailyQuotes.get(i);
                String symbol = dailyAnalysis.dailyQuotes.get(i).ticker;

                GlobalQuote quote = this.extractQuote(symbol);

                // perform analysis
                // we need a quote, ema and the macd values for the last 90 days
                if (quote.globalQuote != null && quote.emaList.size() > 0 && quote.macdList.size() > 0) {

                     this.analyseQuote(dailyQuote, quote);

                    //sleep for a minute
                    try {

                        // the sleeping hear was to do rate limiting from alpha advantage.
                        TimeUnit.MILLISECONDS.sleep(60000L);

                        log.info("Still processing but asleep for a minute");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    log.info("There is no data to process for the day.");
                }
            }
            log.info("Finished analyze task-- " + new Timestamp(System.currentTimeMillis()));
        }
    }


    /*

     */
    private void analyseQuote (DailyQuote dailyQuote , GlobalQuote quote) {
        LocalDate today = LocalDate.parse(quote.recordedDate);
        int daysInCycle = 0;
        Rating rating;
        Trend currentTrend = Trend.NUETRAL;

        for (int m = 0; m < quote.macdList.size(); m++) {
            // we need to break before we hit an index out of bounds exception
            if ((m + 1) == quote.macdList.size()) {
                break;
            }
            MACD current = quote.macdList.get(m);
            MACD previousDay = quote.macdList.get(m + 1);

            Double currentValue = Double.parseDouble(current.macdHist);
            Double previousValue = Double.parseDouble(previousDay.macdHist);

            // the current value is greater than zero than we are either in an upward trend, upward peak, or upward fall.
            // if we are in an upward trend we will see three (arbitrary) days of the previous values being lower or within decimals than the days before
            if (currentValue >= 0) {

                // possible upward trend
                if (currentValue >= previousValue) {
                    daysInCycle++;
                    currentTrend = Trend.POSITIVEUPWARD;

                    if (daysInCycle >= 3) {
                        dailyQuote.trend = currentTrend;
                        dailyQuote.rating = new BuyRating(true);
                        break;
                    }
                }
                // possible downward trend
                else if (currentValue <= previousValue) {
                    daysInCycle++;
                    currentTrend = Trend.POSITIVEDOWNWARD;

                    if (daysInCycle >= 3) {
                        dailyQuote.trend = currentTrend;
                        dailyQuote.rating = new SellRating(true);
                        break;
                    }
                }

            } else {
                // if the current value is less than zero than we are either in a downward trend, downward peak, or downward fall.
                // possible upward trend
                if (currentValue >= previousValue) {

                }
                // possible downward trend
                else if (currentValue <= previousValue) {

                }
            }
        }
    }

    /*

     */
    private GlobalQuote extractQuote (String symbol) {
            GlobalQuote quote = new GlobalQuote();

            // check to see if there is already an entry in the database for the quote today/
            // if so then return that so we don't waste an external call
            try {
                quote = globalQuoteDao.getQuoteByTickerAndDateFromDb(symbol);
                if (quote.id != null) {
                }
            } catch (Exception ex) {
                log.error("There was an exception retrieving the information from the database: " + ex);
            }

            // if we don't have an entry grab the information externally, save and return this data.
            try {

                // retrieve stock quote information
                quote = restTemplate.getForObject(String.format(ApplicationConstants.AV_GLOBAL_QUOTE, symbol), GlobalQuote.class);
                quote.tickerSymbol = symbol;
                quote.emaList = indicators.ExtractDailyEMA20(restTemplate, symbol);
                quote.macdList = indicators.ExtractDailyMACD(restTemplate, symbol);
                quote.recordedDate = this.date;

                try {
                    // try to save to the mongo db
                    globalQuoteDao.saveQuote(quote);

                } catch (Exception ex) {
                    log.error("There was an error saving to the database: " + ex);
                }

            } catch (Exception ex) {
                log.error("There was an exception grabbing the quote for ticker: " + symbol + " Execption: " + ex);
            }
            log.info("Completed grabbing Quote for ticket symbol: " + symbol);

            return quote;
        }

    /*

     */
    private DailyAnalysis grabTodayAnalysis () {
        DailyAnalysis dailyAnalysis = new DailyAnalysis();

        try {
            dailyAnalysis = dailyAnalysisDao.grabBatchByDateForProcessing(this.date);
        } catch (Exception ex) {
            log.info("There was an issue grabbing the batch for processing");
        }

        return dailyAnalysis;
    }
}
