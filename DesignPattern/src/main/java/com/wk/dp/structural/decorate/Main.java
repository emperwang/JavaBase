package com.wk.dp.structural.decorate;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 14:36
 * @Description
 */
public class Main {
    public static void main(String[] args) {
        Engineer engineer = new Engineer();
        TShirtDecorate shirtDecorate = new TShirtDecorate(engineer);
        BeltDecorate beltDecorate = new BeltDecorate(shirtDecorate);
        LeatherDecorate leatherDecorate = new LeatherDecorate(beltDecorate);
        leatherDecorate.wear();
    }
}
