package com.wk.pmapper.fcaps;

import com.wk.entity.fcaps.AmCollectorHostInfo;
import com.wk.entity.fcaps.AmCollectorHostInfoExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AmCollectorHostInfoMapper {
    int countByExample(AmCollectorHostInfoExample example);

    int deleteByExample(AmCollectorHostInfoExample example);

    int deleteByPrimaryKey(Integer collectorId);

    int insert(AmCollectorHostInfo record);

    int insertSelective(AmCollectorHostInfo record);

    List<AmCollectorHostInfo> selectByExample(AmCollectorHostInfoExample example);

    AmCollectorHostInfo selectByPrimaryKey(Integer collectorId);

    int updateByExampleSelective(@Param("record") AmCollectorHostInfo record, @Param("example") AmCollectorHostInfoExample example);

    int updateByExample(@Param("record") AmCollectorHostInfo record, @Param("example") AmCollectorHostInfoExample example);

    int updateByPrimaryKeySelective(AmCollectorHostInfo record);

    int updateByPrimaryKey(AmCollectorHostInfo record);

    String hostInnerIpGet(Integer collectorId);

    List<Integer> getStateIds(@Param("state") String node);
}