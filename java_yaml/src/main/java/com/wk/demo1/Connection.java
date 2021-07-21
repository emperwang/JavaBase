package com.wk.demo1;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author: Sparks
 * @Date: 2021/7/21 9:45
 * @Description
 */
@Setter
@Getter
@ToString
public class Connection {
    private String driverClass;
    private String url;
    private String username;
    private String password;
    private int poolSize;
}
