package com.wk.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * descripiton:
 *
 * @author: wk
 * @time: 19:57 2019/12/31
 * @modifier:
 */
@Setter
@Getter
@ToString
public class User implements Serializable{

    private Integer id;
    private String name;
    private Integer age;
    private String address;
}
