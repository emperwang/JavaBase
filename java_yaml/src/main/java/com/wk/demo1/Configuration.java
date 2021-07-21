package com.wk.demo1;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: Sparks
 * @Date: 2021/7/21 9:38
 * @Description
 */
@Setter
@Getter
@ToString
public class Configuration {
    private Date released;
    private String version;
    private Connection connection;
    private List<String> protocols;
    private Map<String,String> developers;
}
