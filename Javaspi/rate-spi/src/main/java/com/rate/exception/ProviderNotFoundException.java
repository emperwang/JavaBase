package com.rate.exception;

public class ProviderNotFoundException extends RuntimeException {
    public ProviderNotFoundException(){
        super();
    }

    public ProviderNotFoundException(String msg){
        super(msg);
    }
}
