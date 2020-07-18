package com.example.stockapi.dao;


import com.example.stockapi.entity.DailyAnalysis;
import com.example.stockapi.entity.GlobalQuote;
import com.example.stockapi.entity.Symbol;
import com.example.stockapi.models.DailyQuote;
import com.example.stockapi.repository.DailyAnalysisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Slf4j
@Component
public class DailyAnalysisDao {

    private DailyAnalysisRepository dailyAnalysisRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    public DailyAnalysisDao(DailyAnalysisRepository dailyAnalysisRepository) { this.dailyAnalysisRepository = dailyAnalysisRepository;}

    /***
     * Takes in a Quote and saves it to the database.
     * @param quote Quote to save
     */
    public void save(DailyAnalysis dailyAnalysis) {
        log.info("Attempting to save the Daily Analysis to the database -- Timestamp: " + new Timestamp(System.currentTimeMillis()));
        try {

            // do we already have a batch for the day
            DailyAnalysis da = this.grabBatchForProcessing();

            if(da == null) {
                dailyAnalysisRepository.save(dailyAnalysis);
            } else {
                log.info("This daily analysis is already present in the table " + " -- Timestamp: " + new Timestamp(System.currentTimeMillis()));
            }
        } catch (Exception ex){
            log.error("There was an issue saving the daily analysis to the database: " + ex.getMessage()+ " -- Timestamp: " + new Timestamp(System.currentTimeMillis()));
        }
        log.info("Finished saving the Daily Analysis to the database -- Timestamp: " + new Timestamp(System.currentTimeMillis()));
    }

    public DailyAnalysis grabBatchByDateForProcessing (String date) {
        log.info("Attempting to grabBatchForProcessing DailyAnalysis from the db -- Timestamp: " + new Timestamp(System.currentTimeMillis()));
        List<DailyAnalysis> dailyAnalyses = new ArrayList<>();
        DailyAnalysis dailyAnalysis = new DailyAnalysis();


        try {
            List<DailyAnalysis> result = mongoTemplate.query(DailyAnalysis.class)
                    .matching(Query.query(where("recordedDate").is(date)))
                    .all();

            // did we find any?
            if(result.size() > 0){
                log.info("quote was found returning...");
                dailyAnalysis = result.get(0);
            } else {
                log.info("No data was found");
            }

        } catch (Exception ex){
            log.error("There was an issue saving the global quote to the database: " + ex.getMessage()+ " -- Timestamp: " + new Timestamp(System.currentTimeMillis()));
        }

        log.info("Finished grabBatchForProcessing DailyAnalysis from the db -- Timestamp: " + new Timestamp(System.currentTimeMillis()));
        return dailyAnalysis;
    }
}
