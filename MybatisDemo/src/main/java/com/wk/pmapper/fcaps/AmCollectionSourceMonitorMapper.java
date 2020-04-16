package com.wk.pmapper.fcaps;

import com.wk.entity.fcaps.AmCollectionSourceMonitor;
import com.wk.entity.fcaps.AmCollectionSourceMonitorExample;
import com.wk.entity.fcaps.AmCollectionSourceMonitorKey;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

    int clsUnclearedDisconnected(@Param("sourceId") String sourceId, @Param("date") Date date);

    int setUnclearedDisconnectedAndAlarmTime(@Param("sourceId") String sourceId,@Param("date") Date date);

    int setUnclearedIdleAndTimeAndDuration(@Param("sourceId")String sourceId,@Param("date") Date date,@Param("duration") Integer duration);

    int clearUnclearedIdle(@Param("sourceId")String sourceId,@Param("date") Date date);

    // 进行更新操作
    int updateBatch(@Param("tims") Map<String,Date> times);

    // 不存在则插入，存在则更新。postgresql-95 版本之后才有的功能
    int insertOnUpdate(@Param("sid") String sid,@Param("utime") Date utime,@Param("ftime") Date ftime);

    // 批量更新操作
    int insertOnUpdateBatch(@Param("ids") Map<String, Date> map, @Param("utime") Date ut);
}