package com.wk.pmapper.fcaps;

import com.wk.entity.fcaps.AmCollectionSourceMonitor;
import com.wk.entity.fcaps.AmCollectionSourceMonitorExample;
import com.wk.entity.fcaps.AmCollectionSourceMonitorKey;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface AmCollectionSourceMonitorMapper {
    int countByExample(AmCollectionSourceMonitorExample example);

    int deleteByExample(AmCollectionSourceMonitorExample example);

    int deleteByPrimaryKey(AmCollectionSourceMonitorKey key);

    int insert(AmCollectionSourceMonitor record);

    int insertSelective(AmCollectionSourceMonitor record);

    List<AmCollectionSourceMonitor> selectByExample(AmCollectionSourceMonitorExample example);

    AmCollectionSourceMonitor selectByPrimaryKey(AmCollectionSourceMonitorKey key);

    Date getHeatbeatTime(@Param("sourceId") String sourceId);

    int updateByExampleSelective(@Param("record") AmCollectionSourceMonitor record, @Param("example") AmCollectionSourceMonitorExample example);

    int updateByExample(@Param("record") AmCollectionSourceMonitor record, @Param("example") AmCollectionSourceMonitorExample example);

    int updateByPrimaryKeySelective(AmCollectionSourceMonitor record);

    int updateByPrimaryKey(AmCollectionSourceMonitor record);

    int updateCheckpointAndSeqById(AmCollectionSourceMonitor record);

    List<String> selectSourceIds();

    Date getCheckPointByKey(AmCollectionSourceMonitorKey key);

    int updateMonitorHeartBeat(AmCollectionSourceMonitor record);
}