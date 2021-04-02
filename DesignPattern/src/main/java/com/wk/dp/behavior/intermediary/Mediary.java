package com.wk.dp.behavior.intermediary;

import java.util.List;

/**
 * @author: ekiawna
 * @Date: 2021/4/2 10:58
 * @Description
 */
// 中介找房子的例子
public interface Mediary {
    public void deregister(House house);
    public void register(House house);
    public List<House> find(int price);
}
