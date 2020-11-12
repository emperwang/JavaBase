package com.wk.mvel;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.mvel2.MVEL;

/**
 * @author: wk
 * @Date: 2020/11/11 13:40
 * @Description
 */
public class MvelTest {

    @Test
    public void testJson(){
        String str= "{\"uUID\": \"38e4922b3ebc4c809263b93912da63ba\", \"pFlag\": \"No\", \"fmTime\": \"2020-10-20 16:46:33\", \"pVFlag\": \"VNF\", \"rNeUID\": \"1101432X2MNE2FA6E490974A409C\", \"\n" +
                "addInfo\": \"ObjectInstance:ESMService;AdditionalText:36.0Snapshots Availability is 36.0Snapshots Availability is\", \"alarmId\": \"91ca6627941829fc12922640293c" +
                "525a\", \"rNeName\": \"眼科RRU1\", \"rNeType\": \"RRU-LTE\", \"alarmSeq\": 511, \"isFilter\": false, \"province\": \"GD\", \"sourceID\": \"ems001\", \"vendorId\": \"ER\", \"vmIdLis" +
                "t\": \"\", \"alarmType\": \"投诉告警\", \"eventTime\": \"2020-07-24 21:05:08\", \"objectUID\": \"1101432X2MNE2FA6E490974A409C\", \"alarmTitle\": \"omc文件方式同步告警\", \"da" +
                "taSource\": \"EMS\", \"domainType\": \"CT\", \"hostIdList\": \"\", \"localOwner\": true, \"objectName\": \"眼科FE\", \"objectType\": \"MME\", \"regionPath\": \"/0HN/0GD\", \"todoNo" +
                "tify\": true, \"alarmStatus\": 1, \"isDuplicate\": false, \"todoPushGui\": true, \"locationInfo\": \"36.0Snapshots Availability is 36.0Snapshots Availability is -Pr" +
                "obableCause(OSS)= (Linux) bjenmms01 - (Snapshots Status) Snapshots Status - -EventType(OSS)=bjenmms01 - Snapshots Status - Snapshots Availability\", \"origS" +
                "everity\": 3, \"standardFlag\": false, \"subObjectUID\": \"1101432X2MNE2FA6E490974A409C\", \"localProducer\": true, \"subObjectName\": \"眼科FE1\", \"subObjectType\": \"M" +
                "ME\", \"todoReportOss\": true, \"collectionTime\": \"2020-10-20 16:46:31\", \"initialAlarmId\": \"111\", \"operateDataType\": \"alarm\", \"specificProblem\": \"网元问题\", \"" +
                "todoCorrelation\": true, \"specificProblemID\": \"126\"}";
        final JSONObject jsonObject = JSONObject.parseObject(str);
        String mvel = "{\"1101432X2MNE2FA6E490974A409C\"} contains objectUID";
        final Object eval = MVEL.eval(mvel, jsonObject);
        System.out.println(jsonObject.get("origSeverity"));
        System.out.println(eval);

        String mvel2 = "\"38e4922b3ebc4c809263b93912da63ba\" == uUID && \"MME\"== objectType && 3 == origSeverity";
        final Object eval1 = MVEL.eval(mvel2, jsonObject);
        System.out.println(eval1);
    }
}
