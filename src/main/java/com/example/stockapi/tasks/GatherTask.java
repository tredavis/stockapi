package com.example.stockapi.tasks;

import com.example.stockapi.contant.ApplicationConstants;
import com.example.stockapi.dao.DailyAnalysisDao;
import com.example.stockapi.dao.GlobalQuoteDao;
import com.example.stockapi.dao.SymbolDao;
import com.example.stockapi.entity.DailyAnalysis;
import com.example.stockapi.entity.GlobalQuote;
import com.example.stockapi.entity.Symbol;
import com.example.stockapi.models.DailyQuote;
import com.example.stockapi.utility.Indicators;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

/*

 */
@Slf4j
@Component
public class GatherTask extends TimerTask {

    @Autowired
    private SymbolDao symbolDao;

    @Autowired
    private DailyAnalysisDao dailyAnalysisDao;

    RestTemplate restTemplate = new RestTemplate();

    public GatherTask(SymbolDao symbolDao, DailyAnalysisDao dailyAnalysisDao) {
        this.symbolDao = symbolDao;
        this.dailyAnalysisDao = dailyAnalysisDao;
    }

    public void run(){
        log.info("Loading in the info for the symbols: " + new Timestamp(System.currentTimeMillis()));
        DailyAnalysis dailyAnalysis = new DailyAnalysis();
        List<Symbol> symbols = new ArrayList<>();

        // grab unique symbols
        try{
            symbols = symbolDao.grabUniqueSymbolsFromDb();
        } catch (Exception ex){
            log.error("There was an error attempting to grab the symbols " + ex);
        }

        // walk through symbols to create a daily instance
        try {
            for (int i = 0; i < symbols.size(); i++) {
                log.info("Adding ticker symbol name to daily analysis - " + symbols.get(i) + " " + new Timestamp(System.currentTimeMillis()));

                DailyQuote dailyQuote = new DailyQuote();
                dailyQuote.ticker = ""+symbols.get(i)+"";
                dailyQuote.ticker = dailyQuote.ticker.trim();
                dailyAnalysis.dailyQuotes.add(dailyQuote);
            }
        } catch(Exception ex) {
            log.error("There was an error walking through the ticker symbols -- " + ex);
        }

        // save daily analysis to database
        try{
            dailyAnalysisDao.save(dailyAnalysis);
        } catch(Exception ex) {
            log.error("There was an error sending the daily analysis to save");
        }

        //send text message letting me know this was completed.

        log.info("Finished loading in the info for the ticker symbols: " + new Timestamp(System.currentTimeMillis()));
    }
}
