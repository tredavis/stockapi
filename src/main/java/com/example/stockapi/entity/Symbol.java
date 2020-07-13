package com.example.stockapi.entity;

import com.example.stockapi.utility.Rating;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Data
@ToString
public class Symbol {

    @Id
    public String id;

    public String symbol;

    public String Name;

    public Rating rating;

    private void setRating() {
        this.rating = null;

        /*if() {

        } else if () {

        } else {

        }*/
    }

}
