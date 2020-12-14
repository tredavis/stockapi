## Stock Api
This is an application that once running will provide daily analysis of stocks. Eventually it will provide the ability to send messages/notifications to a phone number of your choosing.

# To run this application you will need the following: 
1. MongoDB installed -> https://docs.mongodb.com/manual/installation/
2. Java SDK -> Preferrable the latest.

# Once running endpoints are hosted on port 8080

# Examples 

Endpoint: http://localhost:8080/stock/search-ticker/teladoc

Will return a list of tickers that are close to 'teladoc'
[
    {
        "1. symbol": "TDOC",
        "2. name": "Teladoc Health Inc.",
        "3. type": "Equity",
        "4. region": "United States",
        "5. marketOpen": "09:30",
        "6. marketClose": "16:00",
        "7. timezone": "UTC-05",
        "8. currency": "USD",
        "9. matchScore": "0.7273"
    },
]

Endpoint: http://localhost:8080/stock/grab-quote/tdoc

Will return tdoc with additional meta data while also saving the data to a mongo db database collection.

