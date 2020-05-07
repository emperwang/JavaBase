package com.wk.exUtil;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BaseException extends Throwable {
    private int code;
    private String message;
    private IResponseEnum responseEnum;
    Object[] args;

    public BaseException(String message){
        super(message);
        this.message = message;
    }

    public BaseException(IResponseEnum responseEnum, Object[] args, String message){
        super(message);
        this.message = message;
        this.responseEnum = responseEnum;
        this.args = args;
    }

    public BaseException(IResponseEnum responseEnum, Object[] args, String message, Throwable cause){
        super(message, cause);
        this.message = message;
        this.responseEnum = responseEnum;
        this.args = args;
    }
}
