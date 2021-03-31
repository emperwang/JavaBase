package com.wk.dp.creative.singleton;

/**
 * @author: ekiawna
 * @Date: 2021/3/31 17:38
 * @Description
 */
public class EnumSingleMain {
    public static void main(String[] args) {
        // 测试枚举的单例
        System.out.println(SingletonEnum.SINGLETON.getUser());
        System.out.println(SingletonEnum.SINGLETON.getUser());
    }
}
