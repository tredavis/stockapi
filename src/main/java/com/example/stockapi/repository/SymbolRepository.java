package com.example.stockapi.repository;

import com.example.stockapi.entity.Symbol;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SymbolRepository extends MongoRepository<Symbol, String> {

}