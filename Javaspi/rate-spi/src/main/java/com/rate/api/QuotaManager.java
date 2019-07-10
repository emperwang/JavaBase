package com.rate.api;

import java.util.List;

public interface QuotaManager {

    List<Quote> getquotes(String baseCurrency);
}
