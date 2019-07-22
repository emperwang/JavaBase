package com.wk.httpClientUtil;

/**
 *  restful 请求方法
 */
public enum HttpMethods {
    GET(1,"get"),

    POST(2, "post"),

    DELETE(3,"delete"),

    PUT(4,"put"),

    TRACE(5,"trace"),

    PATCH(6,"patch"),

    OPTION(7,"option")
    ;

    private int num;
    private String name;
    private HttpMethods(int num, String name){
        this.num = num;
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public String getName() {
        return name;
    }
}
