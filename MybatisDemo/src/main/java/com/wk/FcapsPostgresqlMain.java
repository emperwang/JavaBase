package com.wk;

import com.wk.entity.fcaps.AmCollectionSourceMonitor;
import com.wk.entity.fcaps.AmCollectorSource;
import com.wk.entity.fcaps.AmCollectorSourceVo;
import com.wk.pmapper.fcaps.AmCollectionSourceMonitorMapper;
import com.wk.pmapper.fcaps.AmCollectorSourceMapper;
import com.wk.util.PostgresqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;

import java.util.List;

public class FcapsPostgresqlMain {
    private static Logger log = Logger.getLogger(FcapsPostgresqlMain.class);
    private static SqlSessionFactory sessionFactory = PostgresqlUtil.getSessionFactory();

    public static void main(String[] args) {
        getAllMonitor();
//        getVoSource();
    }


    public static void getVoSource(){
        SqlSession sqlSession = sessionFactory.openSession();
        AmCollectorSourceMapper sourceMapper = sqlSession.getMapper(AmCollectorSourceMapper.class);

        AmCollectorSource source = new AmCollectorSource();
        source.setCollectorId(1);
        source.setSourceId("v1");

        List<AmCollectorSourceVo> lists = sourceMapper.getSourceVo(source);

        if (lists != null && lists.size() > 0){
            lists.forEach(v -> {
                log.debug("====================================================");
               log.debug("FmHeartBeat =" + v.getFmHeartBeatTime());
               log.debug("sourceId = " + v.getSourceId());
               log.debug("updateTime= " +v.getUpdateTime());
               log.debug("====================================================");
            });
        }

        sqlSession.close();
    }

    public static void getAllMonitor(){
        SqlSession sqlSession = sessionFactory.openSession();
        AmCollectionSourceMonitorMapper sourceMonitorMapper = sqlSession
                .getMapper(AmCollectionSourceMonitorMapper.class);
        List<AmCollectionSourceMonitor> res = sourceMonitorMapper.selectByExample(null);
        log.debug(res.toString());
        sqlSession.close();
    }
}
