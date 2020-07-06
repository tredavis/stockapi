package com.example.stockapi.Utilities;

import java.lang.reflect.Executable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.example.stockapi.Constants.ApplicationConstants;
import com.example.stockapi.Models.SubModels.EMA;
import com.example.stockapi.Models.SubModels.MACD;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class Indicators {

    @Autowired
    private ApplicationConstants applicationConstants;

    /***
     *
     * @param restTemplate
     * @param symbol
     * @return
     */
    public List<MACD> ExtractDailyMACD(RestTemplate restTemplate, String symbol) {
        Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
        log.info("Grabbing Daily MACD for ticker symbol: " + symbol + "Timestamp -- " + timeStamp);

        List<MACD> returnList = new ArrayList<MACD>();
        final String MACDURL = String.format(applicationConstants.AV_MACD_DAILY, symbol);

        try {

            final ResponseEntity<String> response = restTemplate.getForEntity(MACDURL + "/1", String.class);
            // check the status code of the response
            if (response.getStatusCode() == HttpStatus.OK) {

                // grab the response body
                JSONObject result = new JSONObject();
                try {
                    result = new JSONObject(response.getBody());
                } catch (final JSONException e) {
                    System.out.println(e);
                }

                // begin parsing through each day
                JSONObject days = new JSONObject();
                try {
                    days = result.getJSONObject("Technical Analysis: MACD");
                } catch (final Exception e) {
                    // TODO: handle exception
                }

                final Iterator day = days.keys();
                Integer count = 0;
                while (day.hasNext() && count < 90) {
                    // loop to get the dynamic key
                    final String currentDynamicKey = (String) day.next();

                    JSONObject currentDynamicValue = new JSONObject();
                    try {
                        // get the value of the dynamic key
                        currentDynamicValue = days.getJSONObject(currentDynamicKey);
                        returnList.add(new MACD(currentDynamicKey, currentDynamicValue.getString("MACD"),
                                currentDynamicValue.getString("MACD_Hist"), currentDynamicValue.getString("MACD_Signal")));
                    } catch (final Exception e) {
                        // TODO: handle exception
                    }

                    count++;
                }
            } else {

                // Do something
            }
        } catch (Exception ex) {
            log.error("There was an error grabbing the MACD for ticker symbol: " + symbol + " Execption: " + ex.getMessage());
        }


        log.info("Completed grabbing Daily MACD for ticker symbol: " + symbol + "Timestamp -- " + new Timestamp(System.currentTimeMillis()));
        return returnList;
    }

    /***
     *
     * @param restTemplate
     * @param symbol
     * @return
     */
	public List<EMA> ExtractDailyEMA10(RestTemplate restTemplate, String symbol) {
        Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
        log.info("Grabbing Daily EMA(20) for ticker symbol: " + symbol + "Timestamp -- " + timeStamp);

        List<EMA> returnList = new ArrayList<EMA>();
        final String EMAURL = String.format(applicationConstants.AV_EMA_10, symbol);

        try {
            final ResponseEntity<String> response = restTemplate.getForEntity(EMAURL + "/1", String.class);

            // check the status code of the response
            if (response.getStatusCode() == HttpStatus.OK) {

                // grab the response body
                JSONObject result = new JSONObject();
                try {
                    result = new JSONObject(response.getBody());
                } catch (final JSONException e) {
                    System.out.println(e);
                }

                // begin parsing through each day
                JSONObject days = new JSONObject();
                try {
                    days = result.getJSONObject("Technical Analysis: EMA");
                } catch (final Exception e) {
                    // TODO: handle exception
                }

                final Iterator day = days.keys();
                Integer count = 0;
                while (day.hasNext() && count < 90) {
                    // loop to get the dynamic key
                    final String currentDynamicKey = (String) day.next();

                    JSONObject currentDynamicValue = new JSONObject();
                    try {
                        // get the value of the dynamic key
                        currentDynamicValue = days.getJSONObject(currentDynamicKey);
                        returnList.add(new EMA(currentDynamicKey, currentDynamicValue.getString("EMA")));
                    } catch (final Exception e) {
                        // TODO: handle exception
                    }

                    count++;
                }
            } else {

                // Do something
            }
        } catch (Exception ex) {
            log.error("There was an error grabbing the EMA(10) for ticker symbol: " + symbol + " Execption: " + ex.getMessage());
        }

        log.info("Completed grabbing Daily EMA(20) for ticker symbol: " + symbol + "Timestamp -- " + new Timestamp(System.currentTimeMillis()));
        return returnList;
	}
}