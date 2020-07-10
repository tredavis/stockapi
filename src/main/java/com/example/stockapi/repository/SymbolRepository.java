package com.example.stockapi.repository;

import com.example.stockapi.entity.Symbol;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SymbolRepository extends MongoRepository<Symbol, String> {

}