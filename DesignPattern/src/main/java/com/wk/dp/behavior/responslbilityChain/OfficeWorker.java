package com.wk.dp.behavior.responslbilityChain;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 16:24
 * @Description
 */
// 职员请假,需要一系列的 领导批准; 领导的处理可以简化为一个处理器链, 把职员的请求给各个领导处理
public class OfficeWorker {
    private Manager manager;

    public OfficeWorker(Manager manager){
        this.manager = manager;
    }

    public void requestLeave(int days){
        manager.process(days);
    }
}
