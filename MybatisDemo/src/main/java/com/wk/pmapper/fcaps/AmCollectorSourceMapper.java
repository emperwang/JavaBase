package com.wk.pmapper.fcaps;

import com.wk.entity.fcaps.AmCollectorSource;
import com.wk.entity.fcaps.AmCollectorSourceExample;
import com.wk.entity.fcaps.AmCollectorSourceVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AmCollectorSourceMapper {
    int countByExample(AmCollectorSourceExample example);

    int deleteByExample(AmCollectorSourceExample example);

    int deleteByPrimaryKey(String sourceId);

    int insert(AmCollectorSource record);

    int insertSelective(AmCollectorSource record);

    List<AmCollectorSource> selectByExample(AmCollectorSourceExample example);

    List<String> getCustomStatusIds(@Param("state") String state);

    AmCollectorSource selectByPrimaryKey(String sourceId);

    List<AmCollectorSourceVo> getSourceVo();

    int updateByExampleSelective(@Param("record") AmCollectorSource record, @Param("example") AmCollectorSourceExample example);

    int updateByExample(@Param("record") AmCollectorSource record, @Param("example") AmCollectorSourceExample example);

    int updateByPrimaryKeySelective(AmCollectorSource record);

    int updateByPrimaryKey(AmCollectorSource record);
}