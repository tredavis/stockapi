package com.example.stockapi.Repository;

import com.example.stockapi.Models.GlobalQuote;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuoteRepository extends MongoRepository<GlobalQuote, String> {

}
