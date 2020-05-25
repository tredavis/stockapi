package com.example.stockapi.Utilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.example.stockapi.Models.SubModels.EMA;
import com.example.stockapi.Models.SubModels.MACD;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class Extractor {
    
    ///
    public static List<MACD> ExtractMACD(RestTemplate restTemplate, String symbol) {

        List<MACD> returnList = new ArrayList<MACD>();

        final String MACDURL = String.format(
                "https://www.alphavantage.co/query?function=MACD&symbol=%s&interval=daily&series_type=close&apikey=VTPCO5SG7C08XOT5",
                symbol);

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
            while (day.hasNext() && count < 252) {
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

        return returnList;
    }

	public static List<EMA> ExtractEMA(RestTemplate restTemplate, String symbol) {
		List<EMA> returnList = new ArrayList<EMA>();

        final String EMAURL = String.format(
                "https://www.alphavantage.co/query?function=EMA&symbol=%s&interval=daily&time_period=10&series_type=close&apikey=VTPCO5SG7C08XOT5",
                symbol);

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
            while (day.hasNext() && count < 252) {
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

        return returnList;
	}
}