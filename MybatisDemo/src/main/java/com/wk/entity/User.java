package com.wk.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * descripiton:
 *
 * @author: wk
 * @time: 19:06 2019/12/31
 * @modifier:
 */
@Setter
@Getter
@ToString
public class User {
    private int id;
    private String name;
    private int age;
    private String address;
}
