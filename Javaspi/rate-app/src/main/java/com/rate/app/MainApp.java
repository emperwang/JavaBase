package com.rate.app;

import com.rate.ExchangeRate;
import com.rate.api.Quote;

import java.util.List;

public class MainApp {
    public static void main(String[] args) {
        ExchangeRate.providers().forEach(provider -> {
            System.out.println("Retreiving USD quotes from provider:"+provider);
            List<Quote> quotes = provider.create().getquotes("123");
            quotes.forEach(quote->{
                System.out.println("ask is:"+quote.getAsk()+" ,currency is:"+quote.getCurrency()+", bid is:"+quote.getBid()+ "," +
                        "data is:" +quote.getDate());
            });

        });
    }
}
