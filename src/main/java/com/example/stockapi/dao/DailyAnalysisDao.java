package com.example.stockapi.dao;


import com.example.stockapi.entity.DailyAnalysis;
import com.example.stockapi.entity.GlobalQuote;
import com.example.stockapi.entity.Symbol;
import com.example.stockapi.repository.DailyAnalysisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
            dailyAnalysisRepository.save(dailyAnalysis);
        } catch (Exception ex){
            log.error("There was an issue saving the daily analysis to the database: " + ex.getMessage()+ " -- Timestamp: " + new Timestamp(System.currentTimeMillis()));
        }
        log.info("Finished saving the Daily Analysis to the database -- Timestamp: " + new Timestamp(System.currentTimeMillis()));
    }


    public DailyAnalysis grabBatchForProcessing () {
        log.info("Attempting to grabBatchForProcessing DailyAnalysis from the db -- Timestamp: " + new Timestamp(System.currentTimeMillis()));
        List<DailyAnalysis> dailyAnalyses = new ArrayList<>();
        DailyAnalysis dailyAnalysis = new DailyAnalysis();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();

        try {
            List<DailyAnalysis> result = mongoTemplate.query(DailyAnalysis.class)
                    .matching(Query.query(where("recordedDate").is(dtf.format(now))))
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