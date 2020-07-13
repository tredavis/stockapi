package com.example.stockapi.repository;

import com.example.stockapi.entity.GlobalQuote;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuoteRepository extends MongoRepository<GlobalQuote, String> {

}
