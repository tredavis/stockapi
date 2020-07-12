package com.example.stockapi.dao;

import com.example.stockapi.entity.GlobalQuote;
import com.example.stockapi.entity.Symbol;
import com.example.stockapi.repository.SymbolRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public  class SymbolDao {

    private SymbolRepository symbolRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    public SymbolDao(SymbolRepository tickerRepository) { this.symbolRepository = symbolRepository;}

    public List<Symbol> grabUniqueSymbolsFromDb () {
        log.info("Attempting to grab unique symbols from the db -- Timestamp: " + new Timestamp(System.currentTimeMillis()));
        List<Symbol> symbols = new ArrayList<>();

        try {

            List<Symbol> result = mongoTemplate.findDistinct("tickerSymbol", GlobalQuote.class, Symbol.class);

            if(result.size() > 0){
                symbols = result;
            } else {
                log.warn("There was no symbols found in the database.");
            }

        } catch (Exception ex) {
            log.error("There was an grabbing the symbols from the db: " + ex.getMessage() + " -- Timestamp: " + new Timestamp(System.currentTimeMillis()));
        }

        log.info("Finished grabbing unique symbols from the db -- Timestamp: " + new Timestamp(System.currentTimeMillis()));
        return symbols;
    }

}
