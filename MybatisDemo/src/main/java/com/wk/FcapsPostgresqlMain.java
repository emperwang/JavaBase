package com.wk;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wk.entity.fcaps.*;
import com.wk.pmapper.fcaps.AmCollectionSourceMonitorMapper;
import com.wk.pmapper.fcaps.AmCollectorHostInfoMapper;
import com.wk.pmapper.fcaps.AmCollectorSourceMapper;
import com.wk.pmapper.fcaps.FieldValueMapper;
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
    private static SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMddHHmmss");

    public static void main(String[] args) throws ParseException {
        //getAllMonitor();
//        getSourceids();
//        getVoSource();
//        BatchUpdate();
//        insertOrUpdate();
//        batchUpdate();
//        hostInfoGetAll();
//        getField();
//        getField2();
//        getHis();
        writeDataToExcelWithHutool();
    }
    /*
    sql:
    insert into standard_alarm (alarm_id,clear_operate_user, alarm_status, alarm_seq, clear_flag, collection_time, event_time, alarm_content, if_confirm, if_comment, clear_time, "latest_arrive_time",if_master, if_slave, update_time) values (322633289,'admin',1,322633270,'f','2020-08-21 15:45:39','2020-08-21 15:45:39','{"pFlag": "Yes", "PVFlag": "vim", "fmTime": "2020-08-21 16:48:51", "addInfo": "probable_cause:165;Project:null;vimvendor:Ericsson", "alarmId": 322633269, "alarmSeq": 322633270, "province": "BJ;TJ;HE;SX;NM;LN;JL;HL;JS;SD;SH;AH;ZJ;FJ;JX;HA;HB;HN;GD;GX;HI;SC;GZ;YN;XZ;SN;GS;QH;NX;XJ;CQ", "sourceID": "pim001", "vendorId": "ER", "vmIdList": "vmids", "alarmType": "Server", "eventTime": "2020-08-21 15:45:39", "objectUID": "210200A00QH185003078", "specialty": "NFV-vIMS", "alarmTitle": "ACPI is in the soft-off state", "dataSource": "PIM", "domainType": "CT", "hostIdList": "hostids", "objectName": "Server", "objectType": "server", "regionPath": "/", "todoNotify": true, "vendorName": "爱立信", "alarmStatus": 1, "isDuplicate": false, "todoPushGui": true, "locationInfo": "locationInfo", "origSeverity": 1, "standardFlag": false, "subObjectUID": "NFV-PIM-01-1/Server_02A3FQH182000293", "subObjectName": "Server", "subObjectType": "server", "todoReportOss": true, "collectionTime": "2020-08-21 15:45:39", "operateDataType": "alarm", "specificProblem": "27001", "todoCorrelation": true, "specificProblemID": "27001","localProducer":true,"localOwner":true,"initialAlarmId":123456, "netManagerAlarmId":1098,"businessType":"businessType","clearOperateUser":"clearOperateUser","isFilter":true,"snssaiList":"snssaiList","nssiList":"nssiList","nssiNameList":"nssiNameList","netManagerAlarmSeverity":"123"}','f','f','2020-08-21 15:45:39','2020-08-21 15:45:39','f','f' ,'2020-08-21 16:37:56');

drop table if exists standard_alarm;
CREATE TABLE standard_alarm (
    alarm_id character varying(36) NOT NULL,
    alarm_status smallint NOT NULL,
    alarm_seq integer NOT NULL,
    clear_flag boolean NOT NULL,
    collection_time timestamp without time zone NOT NULL,
    event_time timestamp without time zone NOT NULL,
    alarm_content jsonb NOT NULL,
    if_confirm boolean DEFAULT false,
    confirm_content jsonb,
    if_comment boolean DEFAULT false,
    comment jsonb,
    clear_time timestamp without time zone,
    clear_operate_user character varying(64) DEFAULT NULL::character varying,
    latest_arrive_time timestamp without time zone NOT NULL,
    if_master boolean DEFAULT false,
    if_slave boolean DEFAULT false,
    slave_correlationid jsonb,
    update_time timestamp without time zone
);

     */
    /*
    把查询出来的数据, 写入到excel文档中
     */
    public static void writeDataToExcelWithHutool(){
        String name = "exportedAlarm"+format2.format(new Date())+".xlsx";
        // 1. 获取表头数据,并写入
        final ExcelWriter writer = ExcelUtil.getWriter("C:\\work\\"+name);
        writer.renameSheet("告警标题");
        writer.setRowHeight(0,30);
        final SqlSession session = sessionFactory.openSession();
        final FieldValueMapper mapper = session.getMapper(FieldValueMapper.class);
        final List<FieldValue> alarmFieldName = mapper.getField2(Arrays.asList("AlarmFieldName","DataSourceType","OrigSeverity"));
        Map<String,String> mapings = new HashMap<>(alarmFieldName.size());
        int i=0;
        for (FieldValue fieldValue : alarmFieldName) {
            mapings.put(fieldValue.getValue(),fieldValue.getCnName());
            writer.setColumnWidth(i++,40);
        }
        System.out.println("get maps size: " + mapings.size());
        writer.addHeaderAlias("alarmId",mapings.get("alarmId"));
        writer.addHeaderAlias("alarmTitle",mapings.get("alarmTitle"));
        writer.addHeaderAlias("clearFlag",mapings.get("clearFlag"));
        writer.addHeaderAlias("ifRoot",mapings.get("ifRoot"));
        writer.addHeaderAlias("ifMaster",mapings.get("ifMaster"));
        writer.addHeaderAlias("ifSlave",mapings.get("ifSlave"));
        writer.addHeaderAlias("localProducer",mapings.get("localProducer"));
        writer.addHeaderAlias("localOwner",mapings.get("localOwner"));
        writer.addHeaderAlias("objectName",mapings.get("objectName"));
        writer.addHeaderAlias("objectType",mapings.get("objectType"));
        writer.addHeaderAlias("alarmType",mapings.get("alarmType"));
        writer.addHeaderAlias("origSeverity",mapings.get("origSeverity"));
        writer.addHeaderAlias("netManagerAlarmSeverity",mapings.get("netManagerAlarmSeverity"));
        writer.addHeaderAlias("dataSource",mapings.get("dataSource"));
        writer.addHeaderAlias("vendorName",mapings.get("vendorName"));
        writer.addHeaderAlias("eventTime",mapings.get("eventTime"));
        writer.addHeaderAlias("latestArriveTime",mapings.get("latestArriveTime"));
        writer.addHeaderAlias("initialAlarmId",mapings.get("initialAlarmId"));
        writer.addHeaderAlias("alarmSeq",mapings.get("alarmSeq"));
        writer.addHeaderAlias("specificProblemID",mapings.get("specificProblemID"));
        writer.addHeaderAlias("specificProblem",mapings.get("specificProblem"));
        writer.addHeaderAlias("objectUID",mapings.get("objectUID"));
        writer.addHeaderAlias("subObjectUID",mapings.get("subObjectUID"));
        writer.addHeaderAlias("subObjectName",mapings.get("subObjectName"));
        writer.addHeaderAlias("subObjectType",mapings.get("subObjectType"));
        writer.addHeaderAlias("locationInfo",mapings.get("locationInfo"));
        writer.addHeaderAlias("addInfo",mapings.get("addInfo"));
        writer.addHeaderAlias("pVFlag",mapings.get("pVFlag"));
        writer.addHeaderAlias("standardFlag",mapings.get("standardFlag"));
        writer.addHeaderAlias("confirmFlag",mapings.get("confirmFlag"));
        writer.addHeaderAlias("specialty",mapings.get("specialty"));
        writer.addHeaderAlias("vmIdList",mapings.get("vmIdList"));
        writer.addHeaderAlias("hostIdList",mapings.get("hostIdList"));
        writer.addHeaderAlias("netManagerAlarmId",mapings.get("netManagerAlarmId"));
        writer.addHeaderAlias("businessType",mapings.get("businessType"));
        writer.addHeaderAlias("clearOperateUser",mapings.get("clearOperateUser"));
        writer.addHeaderAlias("clearTime",mapings.get("clearTime"));
        writer.addHeaderAlias("domainType",mapings.get("domainType"));
        writer.addHeaderAlias("sourceID",mapings.get("sourceID"));
        writer.addHeaderAlias("isFilter",mapings.get("isFilter"));
        writer.addHeaderAlias("pFlag",mapings.get("pFlag"));
        writer.addHeaderAlias("snssaiList",mapings.get("snssaiList"));
        writer.addHeaderAlias("nssiList",mapings.get("nssiList"));
        writer.addHeaderAlias("nssiNameList",mapings.get("nssiNameList"));

        // 2. 定义包装每行数据的bean
        final List<HisField> hisa = mapper.getHisa();
        List<ExcelRowBean> lists = new ArrayList<>();
        // 2.1 设置bean中field对应的header
        for (HisField hisField : hisa) {
            final String alarmContent = hisField.getAlarmContent();
            final JSONObject object = JSON.parseObject(alarmContent);
            final ExcelRowBean excelRowBean = new ExcelRowBean();
            excelRowBean.setAlarmId(hisField.getAlarmId());
            excelRowBean.setAlarmTitle(object.get("alarmTitle")!=null?(String) object.get("alarmTitle"):null);
            excelRowBean.setClearFlag(hisField.isClearFlag()?"已清除":"未清除");
            excelRowBean.setIfRoot(hisField.isIfMaster()&&(!hisField.isIfSlave())?"是":"否");
            excelRowBean.setIfMaster(hisField.isIfMaster()?"是":"否");
            excelRowBean.setIfSlave(hisField.isIfSlave()?"是":"否");
            excelRowBean.setLocalProducer(object.get("localProducer")!=null?
                    (((boolean) object.get("localProducer"))?"是":"否"):null);
            excelRowBean.setLocalOwner(object.get("localOwner")!=null?
                    (((boolean) object.get("localOwner"))?"是":"否"):null);
            excelRowBean.setObjectName(object.get("objectName")!=null?(String) object.get("objectName"):null);
            excelRowBean.setObjectType(object.get("objectType")!=null?(String) object.get("objectType"):null);
            excelRowBean.setAlarmType(object.get("alarmType")!=null?(String) object.get("alarmType"):null);
            excelRowBean.setOrigSeverity(object.get("origSeverity")!=null?mapings.get(((Integer)object.get("origSeverity")).toString()):null);
            excelRowBean.setNetManagerAlarmSeverity(object.get("netManagerAlarmSeverity")!=null?(String) object.get("netManagerAlarmSeverity"):null);
            excelRowBean.setDataSource(object.get("dataSource")!=null?mapings.get((String) object.get("dataSource")):null);
            excelRowBean.setVendorName(object.get("vendorName")!=null?(String) object.get("vendorName"):null);
            excelRowBean.setEventTime(object.get("eventTime")!=null?(String) object.get("eventTime"):null);
            excelRowBean.setLatestArriveTime(hisField.getLatestArriveTime()!=null?format.format(hisField.getLatestArriveTime()):null);
            excelRowBean.setInitialAlarmId(object.get("initialAlarmId")!=null?object.get("initialAlarmId").toString():null);
            excelRowBean.setAlarmSeq(hisField.getAlarmSeq()!=null?hisField.getAlarmSeq().longValue():null);
            excelRowBean.setSpecificProblemID(object.get("specificProblemID")!=null?(String) object.get("specificProblemID"):null);
            excelRowBean.setSpecificProblem(object.get("specificProblem")!=null?(String) object.get("specificProblem"):null);
            excelRowBean.setObjectUID(object.get("objectUID")!=null?(String) object.get("objectUID"):null);
            excelRowBean.setSubObjectUID(object.get("subObjectUID")!=null?(String) object.get("subObjectUID"):null);
            excelRowBean.setSubObjectName(object.get("subObjectName")!=null?(String) object.get("subObjectName"):null);
            excelRowBean.setSubObjectType(object.get("subObjectType")!=null?(String) object.get("subObjectType"):null);
            excelRowBean.setLocationInfo(object.get("locationInfo")!=null?(String) object.get("locationInfo"):null);
            excelRowBean.setAddInfo(object.get("addInfo")!=null?(String) object.get("addInfo"):null);
            excelRowBean.setPVFlag(object.get("PVFlag")!=null?(String) object.get("PVFlag"):null);
            excelRowBean.setStandardFlag(object.get("standardFlag")!=null?((boolean) object.get("standardFlag")?"已标准化":"未标准化"):null);
            excelRowBean.setConfirmFlag(hisField.isIfConfirm()?"已确认":"未确认");
            excelRowBean.setSpecialty(object.get("specialty")!=null?(String) object.get("specialty"):null);
            excelRowBean.setVmIdList(object.get("vmIdList")!=null?(String) object.get("vmIdList"):null);
            excelRowBean.setHostIdList(object.get("hostIdList")!=null?(String) object.get("hostIdList"):null);
            excelRowBean.setNetManagerAlarmId(object.get("netManagerAlarmId")!=null?object.get("netManagerAlarmId").toString():null);
            excelRowBean.setBusinessType(object.get("businessType")!=null?(String) object.get("businessType"):null);
            excelRowBean.setClearOperateUser(hisField.getClearOperateUser()!=null?hisField.getClearOperateUser():null);
            excelRowBean.setClearTime(hisField.getClearTime()!=null?format.format(hisField.getClearTime()):null);
            excelRowBean.setDomainType(object.get("domainType")!=null?(String) object.get("domainType"):null);
            excelRowBean.setSourceID(object.get("sourceID")!=null?(String) object.get("sourceID"):null);
            excelRowBean.setIsFilter(object.get("isFilter")!=null?((boolean) object.get("isFilter")?"是":"否"):null);
            // v2
            excelRowBean.setPFlag(object.get("pFlag")!=null?(Boolean.valueOf((String)object.get("pFlag"))?"是":"否"):null);
            excelRowBean.setSnssaiList(object.get("snssaiList")!=null?(String) object.get("snssaiList"):null);
            excelRowBean.setNssiList(object.get("nssiList")!=null?(String) object.get("nssiList"):null);
            excelRowBean.setNssiNameList(object.get("nssiNameList")!=null?(String) object.get("nssiNameList"):null);
            lists.add(excelRowBean);
        }
        // 3. 获取到数据,封装到bean中
        writer.write(lists,true);
        // 4. 把bean数据写入
        writer.close();
        System.out.println("write complete");
    }

    public static void getHis(){
        final SqlSession session = sessionFactory.openSession();
        final FieldValueMapper mapper = session.getMapper(FieldValueMapper.class);
        final List<HisField> hisa = mapper.getHisa();
        for (HisField hisField : hisa) {
            System.out.println(hisField.toString());
        }
    }

    public static void getField2(){
        final SqlSession session = sessionFactory.openSession();
        final FieldValueMapper mapper = session.getMapper(FieldValueMapper.class);
        //final List<FieldValue> alarmFieldName = mapper.getField2("AlarmFieldName","DataSourceType");
        final List<FieldValue> alarmFieldName = mapper.getField2(Arrays.asList("AlarmFieldName","DataSourceType","OrigSeverity"));
        Map<String,String> mapings = new HashMap<>(alarmFieldName.size());
        alarmFieldName.forEach(ff -> {
            System.out.println("cnName="+ff.getCnName());
            System.out.println("value="+ff.getValue());
            mapings.put(ff.getValue(),ff.getCnName());
        });
        System.out.println("-----------------------------------");
        System.out.println(mapings.get("1"));
        session.close();
    }

    public static void getField(){
        final SqlSession session = sessionFactory.openSession();
        final FieldValueMapper mapper = session.getMapper(FieldValueMapper.class);
        final List<Map<String, Object>> fieldName = mapper.getField("AlarmFieldName");
        fieldName.forEach(mm -> {
            System.out.println("---------------------");
            mm.entrySet().forEach(entry -> {
                System.out.println("value,key="+entry.getKey());
                System.out.println("field,cn_name="+entry.getValue());

            });
        });
        session.close();
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
