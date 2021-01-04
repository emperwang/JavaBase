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

    List<Integer> getAllId();

    int batchUpdate(List<User> users);

    int batchUdates(List<User> users);

    int batchInsert(List<User> list);

    int getSeq();

    List<User> getUserBatch(@Param("list") List<User> list, @Param("sage") int sage, @Param("eage") int eage);
}
