package com.wk.dp.behavior.responslbilityChain;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 16:34
 * @Description
 */
public class Main {
    public static void main(String[] args) {
        Manager ceo = new CEO();
        Manager departmentManager = new DepartmentManager(ceo);
        Manager directManager = new DirectManager(departmentManager);

        OfficeWorker worker = new OfficeWorker(directManager);
        //worker.requestLeave(3);
        //worker.requestLeave(8);
        //worker.requestLeave(20);
        worker.requestLeave(40);
    }
}
