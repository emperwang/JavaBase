package com.rate.impl;

import com.rate.api.QuotaManager;
import com.rate.spi.ExchangeRateProvider;

public class YahooExchangeRateProvider implements ExchangeRateProvider{
    @Override
    public QuotaManager create() {
        return new YahooQuoteManagerImpl();
    }
}
