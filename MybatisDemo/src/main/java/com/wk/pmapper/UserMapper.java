package com.wk.pmapper;

import com.wk.entity.User;

import java.util.List;

/**
 * descripiton:
 *
 * @author: wk
 * @time: 19:10 2019/12/31
 * @modifier:
 */
public interface UserMapper {

    List<User> getAll();

    int updateAges(List<User> users);

}
