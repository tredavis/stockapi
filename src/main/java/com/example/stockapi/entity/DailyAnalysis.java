package com.example.stockapi.entity;

import com.example.stockapi.models.DailyQuote;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
public class DailyAnalysis {

    @Id
    public String id;

    public List<DailyQuote> dailyQuotes;

    private String recordedDate;

    public DailyAnalysis() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        this.recordedDate = dtf.format(now);
        this.dailyQuotes = new ArrayList<>();
    }

}
