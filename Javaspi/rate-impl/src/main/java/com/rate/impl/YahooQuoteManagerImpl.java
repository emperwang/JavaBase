package com.rate.impl;

import com.rate.api.QuotaManager;
import com.rate.api.Quote;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class YahooQuoteManagerImpl implements QuotaManager {
    @Override
    public List<Quote> getquotes(String baseCurrency) {
        List<Quote> list = new ArrayList<>();
        Quote quote = new Quote();
        quote.setAsk(new BigDecimal(1));
        quote.setBid(new BigDecimal(2));
        quote.setCurrency("currency--11");
        quote.setDate(LocalDate.now());
        list.add(quote);
        return list;
    }
}
