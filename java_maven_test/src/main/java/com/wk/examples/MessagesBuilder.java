package com.wk.examples;

/**
 * @author: wk
 * @Date: 2020/12/11 16:07
 * @Description
 */
public class MessagesBuilder {

    public String getMessage(String name){
        final StringBuilder builder = new StringBuilder();
        if (name == null || name.trim().length() <= 0){
            builder.append("please provide a name");
        }else{
            builder.append("hello ").append(name);
        }
        return builder.toString();
    }
}
