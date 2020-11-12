package com.wk.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.poi.util.StringUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: wk
 * @Date: 2020/11/5 14:18
 * @Description
 */
public class StaticsInfo {

    public List<Info> generateDate(){
        List<Info> lists = new ArrayList<>();
        final Info info = new Info("reportOSSAlarmDetail",2L,"oss01",null,"oss01");
        final Info info2 = new Info("reportOSSAlarmDetail",3L,"oss02",null,"oss02");

        final Info info3 = new Info("standardAlarm",7L,"host","1101ERHX2SCTBEC7C7539B8CE7","NFV-D-HNGZ-01A-2302-AH17-S-SRV-16");
        final Info info4 = new Info("standardAlarm",7L,"server","1101ERHX2SCTBEC7C7539B8CE9","NFV-D-HNGZ-01A-2302-AH17-S-SRV-15");

//        final Info info5 = new Info("correlationAlarm",6L,"rule_master_slave__2020110411130231",null,"主从告警规则1");
//        final Info info6 = new Info("correlationAlarm",6L,"rule_engineering_suppression_2020110411130231",null,"工程告警1");
//
//        final Info info7 = new Info("reportOSSCorrelationAlarm",6L,null,null,null);
//
//        final Info info8 = new Info("engineeringAlarm",6L,"rule_engineering_suppression_2020110411130231",null,"工程告警1");
//        final Info info9 = new Info("engineeringAlarm",7L,"rule_engineering_suppression_2020110411130242",null,"工程告警2");
//
//        final Info info10 = new Info("reportOSSCorrelationAlarmDetail",3L,"oss01",null,"oss01");
//        final Info info11 = new Info("reportOSSCorrelationAlarmDetail",3L,"oss01",null,"oss01");
//
//        final Info info12 = new Info("duplicatedAlarm",6L,null,null,null);
//
//        final Info info13 = new Info("notStandardAlarm",5L,"server","1101ERHX2SCTBEC7C7539B8CE12","NFV-D-HNGZ-01A-2302-AH17-S-SRV-13");
//        final Info info14 = new Info("notStandardAlarm",5L,"host","1101ERHX2SCTBEC7C7539B8CE10","NFV-D-HNGZ-01A-2302-AH17-S-SRV-14");
//
        final Info info15 = new Info("reportOSSAlarm",5L,null,null,null);
//        final Info info16 = new Info("filteredAlarm",6L,null,null,null);
        final Info info17 = new Info("collectedAlarm",10L,"vim01",null,"vim01采集源");
        final Info info18 = new Info("collectedAlarm",5L,"pin01",null,"pim01采集源");
        lists.add(info);
        lists.add(info2);
        lists.add(info3);
        lists.add(info4);
//        lists.add(info5);
//        lists.add(info6);
//        lists.add(info7);
//        lists.add(info8);
//        lists.add(info9);
//        lists.add(info10);
//        lists.add(info11);
//        lists.add(info12);
//        lists.add(info13);
//        lists.add(info14);
        lists.add(info15);
//        lists.add(info16);
        lists.add(info17);
        lists.add(info18);
        return lists;
    }

    @Test
    public void test1() {
        final List<Info> infos = generateDate();
        if (infos != null && infos.size() > 0) {
            String collectedAlarm = "collectedAlarm";
            String detail = "reportOSSAlarmDetail";
            final Map<String, List<Info>> collect = infos.stream().collect(Collectors.groupingBy(Info::getStatisticalType));
            final List<Info> detas = collect.get(detail);
            collect.remove(detail);
            final List<JSONObject> dtas3 = attr2(detas, detail);

            Long dis = null;
            if (collect.containsKey(collectedAlarm)) {
                dis = collect.get(collectedAlarm).stream().collect(Collectors.summingLong(Info::getCount));
            }
            List<JSONObject> res = new ArrayList<>();
            Long finalDis = dis;
            collect.entrySet().forEach(it -> {
                final String type = it.getKey();
                System.out.println(type);
                //System.out.println(it.getValue());
                //System.out.println("----------------------------------");
                JSONObject object = new JSONObject();
                Long count = it.getValue().stream().collect(Collectors.summingLong(Info::getCount));
                object.put("statisticalType", it.getKey());
                object.put("totalNum", count);
                if (null != finalDis && finalDis > 0) {
                    object.put("rate", (double) Math.round((count.doubleValue() / finalDis.doubleValue()) * 100) / 100);
                }else{
                    object.put("rate",0);
                }
                final List<Info> tmp11 = it.getValue().stream().filter(tm -> {
                    if (!"".equalsIgnoreCase(tm.getAttribute()) && null != tm.getAttribute()) {
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList());
                final List<JSONObject> objects = attr2(tmp11, type);
                object.put("attributes", objects);
                System.out.println(JSON.toJSONString(object, SerializerFeature.WriteMapNullValue));
                if ("reportOSSAlarm".equalsIgnoreCase(type)){
                    object.put("attributes", dtas3);
                }
                System.out.println("----------------------------------");
                res.add(object);
            });
            final JSONArray ret = new JSONArray();
            ret.addAll(res);
            System.out.println(JSON.toJSONString(ret, SerializerFeature.WriteMapNullValue));
        }
    }

    public List<JSONObject> attr2(List<Info> tmp11,String type){
        List<JSONObject> tmp1 = new ArrayList<>();
        if (tmp11 != null && tmp11.size() > 0) {
            final Map<String, List<Info>> collect1 = tmp11.stream().collect(Collectors.groupingBy(Info::getAttribute));
            collect1.entrySet().forEach(it2 -> {
                JSONObject object1 = new JSONObject();
                object1.put("attributeID", it2.getKey());
                if ("standardAlarm".equalsIgnoreCase(type) || "notStandardAlarm".equalsIgnoreCase(type)){
                    object1.put("attributeName", null);
                }else {
                    object1.put("attributeName", it2.getValue().get(0).getAttributeCnName());
                }
                final Long ccut = it2.getValue().stream().collect(Collectors.summingLong(Info::getCount));
                object1.put("attributeCount", ccut);

                List<JSONObject> tmp2 = new ArrayList<>();
                final List<Info> tmp22 = it2.getValue().stream().filter(t22 -> {
                    if (!"".equalsIgnoreCase(t22.getSubAttribute()) && null != t22.getSubAttribute()) {
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList());
                if (tmp22 != null && tmp22.size() > 0) {
                    tmp22.stream().forEach(t222 -> {
                        JSONObject object2 = new JSONObject();
                        object2.put("subattributeId", t222.getSubAttribute());
                        object2.put("subItemName", t222.getAttributeCnName());
                        object2.put("subAttributeCount", t222.getCount());
                        System.out.println(JSON.toJSONString(object2, SerializerFeature.WriteMapNullValue));
                        tmp2.add(object1);
                    });
                    System.out.print("tmp2: ---- ");
                    System.out.println(JSON.toJSONString(tmp2, SerializerFeature.WriteMapNullValue));
                    object1.put("subAttributes", tmp2);
                }
                tmp1.add(object1);
            });
        }
        return tmp1;
    }

}


class Info {
    private String statisticalType;
    private Long count;
    private String attribute;
    private String subAttribute;
    private String attributeCnName;


    public Info(String statisticalType, Long count, String attribute, String subAttribute, String attributeCnName) {
        this.statisticalType = statisticalType;
        this.count = count;
        this.attribute = attribute;
        this.subAttribute = subAttribute;
        this.attributeCnName = attributeCnName;
    }

    public String getStatisticalType() {
        return statisticalType;
    }

    public void setStatisticalType(String statisticalType) {
        this.statisticalType = statisticalType;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getSubAttribute() {
        return subAttribute;
    }

    public void setSubAttribute(String subAttribute) {
        this.subAttribute = subAttribute;
    }

    public String getAttributeCnName() {
        return attributeCnName;
    }

    public void setAttributeCnName(String attributeCnName) {
        this.attributeCnName = attributeCnName;
    }

    @Override
    public String toString() {
        return "Info{" +
                "statisticalType='" + statisticalType + '\'' +
                ", count=" + count +
                ", attribute='" + attribute + '\'' +
                ", subAttribute='" + subAttribute + '\'' +
                ", attributeCnName='" + attributeCnName + '\'' +
                '}';
    }
}