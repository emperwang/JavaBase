package com.rate;

import com.rate.exception.ProviderNotFoundException;
import com.rate.spi.ExchangeRateProvider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

public class ExchangeRate {

    private static final String DEFAULT_PROVIDER="";

    public static List<ExchangeRateProvider> providers(){
        List<ExchangeRateProvider> services = new ArrayList<>();
        ServiceLoader<ExchangeRateProvider> load = ServiceLoader.load(ExchangeRateProvider.class);
        load.forEach(exchangeRateProvider -> {
            services.add(exchangeRateProvider);
        });
        return services;
    }

    public static ExchangeRateProvider provider(){
        return provider(DEFAULT_PROVIDER);
    }

    public static ExchangeRateProvider provider(String providerName){
        ServiceLoader<ExchangeRateProvider> loader = ServiceLoader.load(ExchangeRateProvider.class);
        Iterator<ExchangeRateProvider> iterator = loader.iterator();

        while (iterator.hasNext()){
            ExchangeRateProvider next = iterator.next();
            if (providerName.equals(next.getClass().getName())){
                return next;
            }
        }
        throw new ProviderNotFoundException("provider:" +providerName+" not found");
    }

}

























