package com.wk;

import com.wk.entity.fcaps.AmCollectionSourceMonitor;
import com.wk.entity.fcaps.AmCollectorHostInfo;
import com.wk.entity.fcaps.AmCollectorSource;
import com.wk.entity.fcaps.AmCollectorSourceVo;
import com.wk.pmapper.fcaps.AmCollectionSourceMonitorMapper;
import com.wk.pmapper.fcaps.AmCollectorHostInfoMapper;
import com.wk.pmapper.fcaps.AmCollectorSourceMapper;
import com.wk.util.PostgresqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class FcapsPostgresqlMain {
    private static Logger log = Logger.getLogger(FcapsPostgresqlMain.class);
    private static SqlSessionFactory sessionFactory = PostgresqlUtil.getSessionFactory();
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws ParseException {
        //getAllMonitor();
//        getSourceids();
//        getVoSource();
//        BatchUpdate();
//        insertOrUpdate();
//        batchUpdate();
        hostInfoGetAll();
    }

    public static void hostInfoGetAll(){
        SqlSession sqlSession = sessionFactory.openSession();
        AmCollectorHostInfoMapper hostInfoMapper = sqlSession.getMapper(AmCollectorHostInfoMapper.class);
        List<AmCollectorHostInfo> hosts = hostInfoMapper.selectByExample(null);
        List<Integer> ids = hostInfoMapper.getStateIds("MASTER");
        ids.forEach(System.out::println);
        // hosts.forEach(System.out::println);
        sqlSession.close();
    }

    public static void batchUpdate() throws ParseException {
        SqlSession sqlSession = sessionFactory.openSession();
        AmCollectionSourceMonitorMapper sourceMapper = sqlSession.getMapper(AmCollectionSourceMonitorMapper.class);
        Map<String,Date> map = new HashMap<>();
        map.put("vim01",new Date());
        map.put("ems1",new Date());
        Date date = format.parse("2019-00-00 12:12:21");
        sourceMapper.insertOnUpdateBatch(map,date);
        sqlSession.commit();
        sqlSession.close();
    }

    /**
     *  如果数据存在则更新，数据不存在则插入操作。postgresl-9.5之后提供的新功能
     * @throws ParseException
     */
    public static void insertOrUpdate() throws ParseException {
        SqlSession sqlSession = sessionFactory.openSession();
        AmCollectionSourceMonitorMapper sourceMapper = sqlSession.getMapper(AmCollectionSourceMonitorMapper.class);
        Date d = new Date();
        Date date = format.parse("2019-00-00 12:12:21");
        sourceMapper.insertOnUpdate("vim01",d,date);
        sqlSession.commit();
        sqlSession.close();
    }
    public static void BatchUpdate(){
        SqlSession sqlSession = sessionFactory.openSession();
        AmCollectionSourceMonitorMapper sourceMapper = sqlSession.getMapper(AmCollectionSourceMonitorMapper.class);
        Map<String,Date> map = new HashMap<>();
        map.put("v1",new Date());
        map.put("ems1",new Date());
        sourceMapper.updateBatch(map);
        sqlSession.commit();
        sqlSession.close();
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

    public static void getSourceids(){
        SqlSession sqlSession = sessionFactory.openSession();
        AmCollectorSourceMapper sourceMapper = sqlSession.getMapper(AmCollectorSourceMapper.class);
        List<String> ids = sourceMapper.getCustomStatusIds("idiidiid");
        List<String> tmp = new ArrayList<>();
        System.out.println("ids size : " + ids.size());
        System.out.println("result : " + ids.containsAll(tmp));

        sqlSession.close();
    }
}
