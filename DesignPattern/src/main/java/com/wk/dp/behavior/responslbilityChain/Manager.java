package com.wk.dp.behavior.responslbilityChain;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 16:26
 * @Description
 */
public abstract class Manager {
    private Manager higerManager;

    public Manager(Manager manager){
        higerManager = manager;
    }
    // 参数为请假的天数
    abstract void process(int days);

    public Manager getHigerManager() {
        return higerManager;
    }

    public void setHigerManager(Manager higerManager) {
        this.higerManager = higerManager;
    }
}
