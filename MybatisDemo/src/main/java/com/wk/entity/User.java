package com.wk.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
public class User {
    private Integer id;
    private String name;
    private Integer age;
    private String address;
}
