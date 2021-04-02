package com.wk.dp.behavior.intermediary;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author: ekiawna
 * @Date: 2021/4/2 10:59
 * @Description
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class House {
    private int price;
    private String address;
    private int size;
}
