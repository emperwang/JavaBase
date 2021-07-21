package com.wk.bd;

/**
 * @author: Sparks
 * @Date: 2021/7/20 15:58
 * @Description
 */
public class BizMethod {

    public String queryUserInfo(String id, String name){
        return new StringBuilder("query user info, id: ").append(id)
                .append(", name= ").append(name).toString();
    }
    public String spandId(){
        return "biz spanId";
    }
}
