package com.wk.convert;

import com.wk.entity.UserBO;
import com.wk.entity.UserDao;
import com.wk.entity.UserDetailBO;

// mapstruct 生成的代码
public class UserConvertImplgen implements UserConvert {
    public UserConvertImplgen() {
    }

    public UserDao convert(UserBO userBO) {
        if (userBO == null) {
            return null;
        } else {
            UserDao userDao = new UserDao();
            userDao.setId(userBO.getId());
            userDao.setUsername(userBO.getUsername());
            userDao.setPassword(userBO.getPassword());
            return userDao;
        }
    }

    @Override
    public UserDetailBO convertDetail(UserDao userDao) {
        if (userDao == null) {
            return null;
        } else {
            UserDetailBO userDetailBO = new UserDetailBO();
            userDetailBO.setUserId(userDao.getId());
            return userDetailBO;
        }
    }
}
