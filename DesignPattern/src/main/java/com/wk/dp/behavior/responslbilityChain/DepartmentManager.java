package com.wk.dp.behavior.responslbilityChain;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 16:27
 * @Description
 */
public class DepartmentManager extends Manager {

    public DepartmentManager(Manager manager) {
        super(manager);
    }

    @Override
    void process(int days) {
        System.out.println("department manager process");
        if (days >5 && days<=10){
            System.out.println("5<假期<= 10, 批准了");
        }else{
            System.out.println("超过 department manager 权限, 给上级领导处理");
            getHigerManager().process(days);
        }
    }
}
