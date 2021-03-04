package com.wk.pmapper;

import com.wk.entity.User;
import org.apache.ibatis.annotations.Param;

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
    /**
     * 注意: mapper接口不支持  方法的重载
     */
    // 查看是否支持接口重载
    //List<User> getAll(List<Integer> list);

    List<Integer> getAllId();

    int batchUpdate(List<User> users);

    int batchUdates(List<User> users);

    int batchInsert(List<User> list);

    int getSeq();

    List<User> getUserBatch(@Param("list") List<User> list, @Param("sage") int sage, @Param("eage") int eage);
}
