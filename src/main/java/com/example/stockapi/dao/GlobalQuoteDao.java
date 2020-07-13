package com.example.stockapi.dao;

import com.example.stockapi.entity.GlobalQuote;
import com.example.stockapi.repository.QuoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Slf4j
@Component
public class GlobalQuoteDao {

    private QuoteRepository quoteRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    public GlobalQuoteDao(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    /***
     * Takes in a Quote and saves it to the database.
     * @param quote Quote to save
     */
    public void saveQuote(GlobalQuote quote) {
        log.info("Attempting to save the Global Quote to the database -- Timestamp: " + new Timestamp(System.currentTimeMillis()));
        try {
            quoteRepository.save(quote);
        } catch (Exception ex){
            log.error("There was an issue saving the global quote to the database: " + ex.getMessage()+ " -- Timestamp: " + new Timestamp(System.currentTimeMillis()));
        }
        log.info("Finished saving the Global Quote to the database -- Timestamp: " + new Timestamp(System.currentTimeMillis()));
    }

    /***
     * Searches for a ticker symbol and checks to see if we have an entry for the current date. If so return the value out.
     * @param symbol Ticker symbol to search for.
     * @return Returns the quote for the ticker symbol from the database. If it exists.
     */
    public GlobalQuote getQuoteByTickerAndDateFromDb(String symbol) {
        log.info("Attempting getQuoteByTickerAndDate from db -- Timestamp: " + new Timestamp(System.currentTimeMillis()));
        GlobalQuote globalQuote = new GlobalQuote();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();

        try {
            List<GlobalQuote> result = mongoTemplate.query(GlobalQuote.class)
                    .matching(Query.query(where("recordedDate").is(dtf.format(now)).and("tickerSymbol").is(symbol)))
                    .all();

            // did we find any?
            if(result.size() > 0){
               log.info("quote was found returning...");
               globalQuote = result.get(0);
            } else {
                log.info("No data was found");
            }

        } catch (Exception ex){
            log.error("There was an issue saving the global quote to the database: " + ex.getMessage()+ " -- Timestamp: " + new Timestamp(System.currentTimeMillis()));
        }

        log.info("Finished getQuoteByTickerAndDate from db -- Timestamp: " + new Timestamp(System.currentTimeMillis()));
        return globalQuote;
    }

    public GlobalQuote getQuoteByTicker(String symbol){
        GlobalQuote globalQuote = new GlobalQuote();
        /*globalQuote = quoteRepository.findBySymbol(symbol);*/
        return globalQuote;
    }
}
