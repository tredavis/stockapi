package com.example.stockapi.contant;

import org.springframework.stereotype.Component;

/***
 *
 */
@Component
public class ApplicationConstants {
     private final static String APIKey = "VTPCO5SG7C08XOT5";

     public final String AV_GLOBAL_QUOTE = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=%s&apikey=" + APIKey;
     public final String AV_SYMBOL_SEARCH = "https://www.alphavantage.co/query?function=SYMBOL_SEARCH&keywords=%s&apikey=" + APIKey;

     public final String AV_EMA_10 = "https://www.alphavantage.co/query?function=EMA&symbol=%s&interval=daily&time_period=10&series_type=close&apikey=" + APIKey;
     public final String AV_EMA_20 = "https://www.alphavantage.co/query?function=EMA&symbol=%s&interval=daily&time_period=20&series_type=close&apikey=" + APIKey;

     public final String AV_MACD_DAILY = "https://www.alphavantage.co/query?function=MACD&symbol=%s&interval=daily&series_type=close&apikey=" + APIKey;
     public final String AV_MACD_MONTHLY = "https://www.alphavantage.co/query?function=MACD&symbol=%s&interval=monthly&series_type=close&apikey=" + APIKey;

}
