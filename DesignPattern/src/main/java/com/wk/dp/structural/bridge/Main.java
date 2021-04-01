package com.wk.dp.structural.bridge;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 17:28
 * @Description
 */
public class Main {
    public static void main(String[] args) {
        Mem2G mem2G = new Mem2G();
        Mem4G mem4G = new Mem4G();
        MiPhone miPhone = new MiPhone();
        miPhone.setMemory(mem2G);

        MiPhone miPhone1 = new MiPhone();
        miPhone1.setMemory(mem4G);

        HWPhone hwPhone = new HWPhone();
        hwPhone.setMemory(mem2G);

        HWPhone hwPhone1 = new HWPhone();
        hwPhone1.setMemory(mem4G);
    }
}
