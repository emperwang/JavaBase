package com.rate.spi;

import com.rate.api.QuotaManager;

public interface ExchangeRateProvider {
    QuotaManager create();
}
