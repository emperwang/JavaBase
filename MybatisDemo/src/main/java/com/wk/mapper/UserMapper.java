package com.wk.mapper;

import com.wk.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * descripiton:
 *
 * @author: wk
 * @time: 19:10 2019/12/31
 * @modifier:
 */
public interface UserMapper {

    List<User> selectAll();

    /**
     * 使用 注解 指定参数名字
     * @param id
     * @return
     */
    User selectById(@Param("id") Integer id);

    /**
     * 使用 map 其中的key为参数的名字
     * @param
     * @return
     */
    User selectByMap(Map<String,String> map);

    int updateAgeList(List<User> users);

    /**
     *  选择性的更新操作
     * @param user
     * @return
     */
    int updateSelectFieldTrim(User user);

    int updateSelectField(User user);
}
