package com.wk.main;

import com.wk.convert.UserConvert;
import com.wk.entity.UserBO;
import com.wk.entity.UserDao;
import com.wk.entity.UserDetailBO;

public class UserBOMain {
    public static void main(String[] args) {
        final UserBO userBO = new UserBO().setId(1).setUsername("yunai")
                .setPassword("1234");

        final UserDao userDao = UserConvert.INSTANCE.convert(userBO);
        System.out.println(userDao);

        System.out.println("----------------------------------------");
        final UserDao userDao1 = new UserDao().setId(1000);
        final UserDetailBO userDetailBO = UserConvert.INSTANCE.convertDetail(userDao1);
        System.out.println(userDetailBO);
    }
}
