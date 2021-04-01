package com.wk.dp.behavior.responslbilityChain;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 16:27
 * @Description
 */
public class CEO extends Manager {
    public CEO() {
        super(null);
    }

    @Override
    void process(int days) {
        System.out.println("ceo process");
        if (days<=30){
            System.out.println("假期<= 30, 批准了");
        }else{
            System.out.println("请假太长, 不批准");
        }
    }
}
