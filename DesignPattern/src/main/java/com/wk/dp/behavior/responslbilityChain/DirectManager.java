package com.wk.dp.behavior.responslbilityChain;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 16:27
 * @Description
 */
public class DirectManager extends Manager{
    public DirectManager(Manager manager) {
        super(manager);
    }

    @Override
    void process(int days) {
        System.out.println("directManager process");
        if (days <= 5){
            System.out.println("假期小于5天, 批准了.");
        }else {
            System.out.println("超过 direct manager 权限, 给上级领导处理");
            getHigerManager().process(days);
        }
    }
}
