package com.wk.zabbixClient.version1;

import com.alibaba.fastjson.JSONObject;
import com.wk.common.Methods;
import com.wk.common.MethodsParam;
import io.github.hengyunabc.zabbix.api.DefaultZabbixApi;
import io.github.hengyunabc.zabbix.api.Request;
import io.github.hengyunabc.zabbix.api.RequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZabbixClient {
    private static Logger log = LoggerFactory.getLogger(ZabbixClient.class);
    private static String ZabbixAPI="http://192.168.72.35/zabbix/api_jsonrpc.php";
    private static String Name="admin";
    private static String Pass="zabbix";

    public static void main(String[] args) {
        DefaultZabbixApi zabbixApi = new DefaultZabbixApi(ZabbixAPI);
        zabbixApi.init();

        // ZabbixLogin(zabbixApi);
        getHost(zabbixApi);
       // loginIn(zabbixApi);
    }

    private static void loginIn(DefaultZabbixApi zabbixApi){
        Request request = RequestBuilder.newBuilder().method(Methods.UserLogin)
                .paramEntry(MethodsParam.LoginParamUser, "admin")
                .paramEntry(MethodsParam.LoginParamPassword, "zabbix")
                .auth(null).id(1).build();

        JSONObject result = zabbixApi.call(request);
        log.info("login result :"+result.toJSONString());
    }

    private static void getHost(DefaultZabbixApi zabbixApi){
        String[] hostParam = {"hostid","host"};
        String[] interfacs = {"interfaceid","ip"};
        Request request = RequestBuilder.newBuilder().method(Methods.HostGet)
                .paramEntry("output", hostParam).paramEntry("selectInterfaces",interfacs)
                .auth("6daa09c9edda4bb62a15e98bab164400").id(1).build();

        JSONObject result = zabbixApi.call(request);

        log.info("hostGroup is :"+result.toJSONString());
    }

    private static void ZabbixLogin(DefaultZabbixApi zabbixApi) {
        boolean login = zabbixApi.login(Name, Pass);
        log.info("login result is :"+login);
    }
}
