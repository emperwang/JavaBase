package com.wk.zabbixSender.version1;

import io.github.hengyunabc.zabbix.sender.DataObject;
import io.github.hengyunabc.zabbix.sender.SenderResult;
import io.github.hengyunabc.zabbix.sender.ZabbixSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ZabbixSendMsg {
    private static Logger log = LoggerFactory.getLogger(ZabbixSendMsg.class);
    private static String host="192.168.72.35";
    private static int Port=10051;
    public static void main(String[] args) throws IOException {
        ZabbixSender sender = new ZabbixSender(host, Port);

        DataObject dataObject = new DataObject();
        dataObject.setHost(host);
        dataObject.setKey("test_item");
        dataObject.setValue("10");

        dataObject.setClock(System.currentTimeMillis()/1000);

        SenderResult result = sender.send(dataObject);
        boolean success = result.success();
        log.info("send result : "+success);
    }
}
