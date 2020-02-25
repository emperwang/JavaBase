package com.wk.fcaps.socket.client;

import com.alibaba.fastjson.JSON;
import com.wk.fcaps.socket.bean.OrigAlarm;
import com.wk.fcaps.socket.bean.SourceBo;
import com.wk.fcaps.socket.constants.OMCSocketMsgHeaders;
import com.wk.fcaps.socket.constants.SocketPacket;
import com.wk.fcaps.socket.util.DateUtil;
import com.wk.fcaps.socket.util.Socketutil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *  OMC 实时告警数据采集
 *  1. socket进行连接
 *  2. 进行登录操作(重试三次)
 *  3. 登录成功,开始接收数据
 *  4. 如果告警消息不连续进行消息同步
 *        1. 消息不连续数量超过1000,进行文件同步操作
 *              根据响应信息去进行文件下载操作
 *        2. 不超过1000,进行告警消息的同步
 *  5. 60s间隔心跳
 *         心跳相应
 *          如果180s没有收到OMC任何信息，则主动断开与OMC的连接，然后重试
 *
 *
 *  6. 根据接收的告警数据条数进行数据的入库操作
 */
@Slf4j
public class SocketTask extends Thread{

    private SourceBo sourceBo;

    private Integer taskId;

    private Socket socket = null;
    // 请求序列号
    private static AtomicInteger reqId = new AtomicInteger(0);
    // 缓存接收到的告警信息的容器
    List<OrigAlarm> emsAlarms = new ArrayList<>();
    // 上次读取的告警序列号
    private Integer lastAlarmSeq = -1;
    // 本地读取的告警序列号
    private Integer currentAlarmSeq = 0;

    private Integer lastSyncAlarmSeq = 0;
    // 上次读取的时间
    private Date lastReadTime = new Date();
    // 本次读取的时间
    private Date currentReadTime = new Date();

    private Date lastHeartBeatTime = new Date();

    //private NfvConfig nfvConfig = NfvConfig.getConfig();

    //private AlarmMapper alarmMapper;

    //private KafkaProducer kafkaProducer;

    //private VpimReceiverService vpimReceiverService;

    private volatile boolean isRunning = true;

    //private TaskMapper taskMapper;

    private String errorMsg = "Read timed out";

    private Integer errTimes = 0;

    //private RedisService redisService;
	
	//private ExecutorService executorService = ThreadPoolUtil.singlePool();

    private boolean isCheckSeq = true;

    public SocketTask(SourceBo sourceBo, Integer taskId) {
        this.sourceBo = sourceBo;
        this.taskId = taskId;
        //this.alarmMapper = alarmMapper;
        //this.kafkaProducer = kafkaProducer;
        //this.vpimReceiverService = vpimReceiverService;
        //this.taskMapper = taskMapper;
        //this.redisService = redisService;
    }

    @Override
    public void run() {
        startSocketTask();
    }

    public void startSocketTask(){
        // 1. 连接
        try {
            socket = Socketutil.login(sourceBo.getSocketIp(), sourceBo.getSocketPort(), sourceBo.getSocketUser(),
                    sourceBo.getSocketPassword(), OMCSocketMsgHeaders.SocketType);
            startReceiveMsg();
        } catch (Exception e) {
            //taskMapper.updateTaskStatus("failed",taskId);
            log.error("startSocketTask socket login error,err msg is:{}", e.getMessage());
        }
    }

    /**
     *  接收告警消息
     *  1.固定发送心跳
     *  2.接收信息
     *      1. 如果超过180s没有接收到数据，则断开连接，重新连接
     *  3.检测信息的seq号
     *      1. 序号连续则继续
     *      2. 序号不连续
     *          1.超过1000条，则使用文件进行同步
     *          2.超过1000条，则使用信息同步
     */
    public void startReceiveMsg() {
        //  此时认定登录完成
        while (isRunning){
            try {
                checkServerIsAlive();
                // 检查心跳
                checkAndSendHeartBeat();
                checkHeartbeat();
                checkAlreadyReceiveMsg();
                // receive msg
                String alarmMsg = Socketutil.receiveSocket(socket);
                dealSocketResponseInfo(alarmMsg);
            } catch (IOException e) {
                if (errorMsg.equalsIgnoreCase(e.getMessage())) {
                    log.debug("startReceiveMsg method error,err msg is:{}", e.getMessage());
                }else {
                    log.error("startReceiveMsg method error,err msg is:{}", e.getMessage());
                }
                updateErrorTimes(e);
            }catch (Exception e){
                log.error("startReceiveMsg method error,err msg is:{}", e.getMessage());
                updateErrorTimes(e);
            }
        }
        closeSocketConnect();
    }

    public void dealSocketResponseInfo(String msg) throws Exception {
        if (msg != null && msg.length() > 0){
            if (msg.contains(OMCSocketMsgHeaders.ackHeartBeat)) {
                log.info(OMCSocketMsgHeaders.ackHeartBeat);
                lastHeartBeatTime = new Date();
                updateForSokcetHeartBeat();
            } else if (msg.contains(OMCSocketMsgHeaders.ackSyncAlarmMsg)) {
                log.info(msg);
                if (msg.contains("fail")) {
                    SocketPacket msgSyncpacket = OMCSocketMsgHeaders.configMsgSyncPacket(getReqId(), ++lastSyncAlarmSeq);
                    log.info("msg sync msg is: {}",msgSyncpacket.getBody());
                    Socketutil.sendSocket(msgSyncpacket,socket);
                    isCheckSeq = false;
                    return;
                }
                isCheckSeq = true;
            } else if (msg.contains(OMCSocketMsgHeaders.ackLoginAlarm)) {
                log.info(msg);
            } else {
                lastReadTime = currentReadTime;
                currentReadTime = new Date();
                this.errTimes = 0;
                configOMCMsgToBean(msg);
                if (currentAlarmSeq.intValue() == lastSyncAlarmSeq.intValue()) {
                    lastAlarmSeq = currentAlarmSeq;
                }
                // 检查seq是否连续
                log.debug("isCheckSeq:{}",isCheckSeq);
                if (isCheckSeq) {
                    checkSeq();
                    //没有发生补采
                    if (isCheckSeq) {
                        updateAlarmSeqToDB(sourceBo.getSourceId(), currentAlarmSeq);
                    }
                }
            }
        }
    }

    private void checkServerIsAlive(){
        //Integer checkStyle = nfvConfig.getEmsCheckServerStyle();
        Integer checkStyle = 1;
        if (checkStyle >= 0) {
            checkConnect();
        }else {
            checkTimes();
        }
    }


    private void checkTimes(){
        log.info("check Times ... ");
        if (this.errTimes >= 3){
            isRunning = false;
        }
    }

    private void updateErrorTimes(Exception e){
        if (e != null && !errorMsg.equalsIgnoreCase(e.getMessage())){
            this.errTimes += 1;
            log.info("update times .. ,is :{}",errTimes);
        }
    }

    private void checkConnect(){
        log.info("checkConnect ... ");
        if (this.socket == null){
            log.info("socket conncet already closed..");
            isRunning = false;
            return;
        }
        if (!socket.isConnected() || socket.isClosed()){
            log.info("scoket is already closed or not connected..");
            isRunning = false;
            return;
        }
        try {
            socket.sendUrgentData(0xff);
        } catch (Exception e) {
            log.error("checkConnect IOException, msg is : {}",e.getMessage());
            isRunning = false;
        }
    }

    private void checkHeartbeat() throws Exception {
        log.info("checkHeartbeat ......");
        Date date = new Date();
        int readInterval = DateUtil.compareTime(date, lastReadTime);
        int heartBeatInterval = DateUtil.compareTime(date, lastHeartBeatTime);
        if (readInterval >= OMCSocketMsgHeaders.ReceiveTimeOut && heartBeatInterval >= OMCSocketMsgHeaders.ReceiveTimeOut){
            closeSocketConnect();
            try {
                //executorService.submit(this.new updateMonitor());
                updateForSokcetHeartBeat();
                socket = Socketutil.login(sourceBo.getSocketIp(), sourceBo.getSocketPort(), sourceBo.getSocketUser(),
                        sourceBo.getSocketPassword(), OMCSocketMsgHeaders.SocketType);
            } catch (Exception e) {
                updateErrorTimes(e);
                //executorService.submit(this.new updateMonitor());
                updateForSokcetHeartBeat();
                // taskMapper.updateTaskStatus("failed",taskId);
                log.error("checkHeartbeat NfvoException,msg is :{}",e.getMessage());
                log.debug("re-login fail:{}",e.getMessage());
                isRunning = false;
                throw new Exception("socket re-login error ",e);
            }
        }
    }

    private void checkAlreadyReceiveMsg(){
        if (CollectionUtils.isNotEmpty(emsAlarms)){
            int time = DateUtil.compareTime(new Date(), lastReadTime);
            updateMonitorDB(time);
        }
    }

    /**
     *  中断当前socket线程
     */
    public void stopSocketThread(){
        log.info("stop socket thread....");
        isRunning = false;
    }

    /**
     *  序列号检查
     *  1. 正常，则不操作
     *  2. 不正常
     *      1. 序号不连续超过1000条,则使用文件进行同步
     *      2. 序号不连续,不超过1000条,则使用消息进行同步
     *
     */
    public void checkSeq() throws IOException {
        log.info("start checkSeq,lastAlarmSeq is {},currentAlarmSeq is {}",lastAlarmSeq,currentAlarmSeq);
        // 第一次判断
        if (lastAlarmSeq ==null || lastAlarmSeq.intValue() == -1){
            lastAlarmSeq = currentAlarmSeq == 0?0:currentAlarmSeq;
            /*CollectionSourceMonitor monitor = taskMapper.selectByPrimayKey(sourceBo.getSourceId(),  "FM");
            if (null != monitor && null != monitor.getAlarmSeq()) {
                log.info("selectByPrimayKey :{}", monitor.toString());
                lastAlarmSeq = monitor.getAlarmSeq();
            } else {
                lastAlarmSeq = currentAlarmSeq;
            }*/
        }
        // 同步操作
        if (currentAlarmSeq <= lastAlarmSeq){
            lastAlarmSeq = currentAlarmSeq;
            return;
        }
        int count = currentAlarmSeq - lastAlarmSeq;
        if (count == 1){
            lastAlarmSeq = currentAlarmSeq;
            return;
        }

        if (count > 1 && count < 1000){
            lastSyncAlarmSeq = lastAlarmSeq + 1;
            SocketPacket msgSyncpacket = OMCSocketMsgHeaders.configMsgSyncPacket(getReqId(), lastSyncAlarmSeq);
            log.info("msg sync msg is: {}",msgSyncpacket.getBody());
            Socketutil.sendSocket(msgSyncpacket,socket);
            updateAlarmSeqToDB(sourceBo.getSourceId(), lastAlarmSeq);
            isCheckSeq = false;
        }

        if (count >= 1000){
            //FileSyncTask fileSyncTask = new FileSyncTask(lastAlarmSeq, sourceBo, kafkaProducer, vpimReceiverService, alarmMapper, redisService);
            //fileSyncTask.setName("OmcSocket-FileSyncTask-" + sourceBo.getSourceId());
            //fileSyncTask.start();
            lastAlarmSeq = currentAlarmSeq;
        }
    }

    /**
     *  关闭omc-socket连接
     */
    public void closeSocketConnect(){
        try {
            log.info("closeSocketConnect...");
            if (socket != null && socket.isConnected()){
                SocketPacket logOutPacket = OMCSocketMsgHeaders.configLoginOutPacket();
                if (log.isDebugEnabled()){
                    log.debug("logOutPacket :{}",logOutPacket.getBody());
                }
                Socketutil.sendSocket(logOutPacket,socket);
                Thread.sleep(1000);
                socket.close();
                log.info(OMCSocketMsgHeaders.closeConnAlarm);
            }
        } catch (InterruptedException e) {
            log.error("closeSocketConnect method InterruptedException,msg is:{}",e.getMessage());
        } catch (IOException e) {
            log.error("closeSocketConnect method IOException,msg is:{}",e.getMessage());
        }catch (Exception e){
            log.error("closeSocketConnect method Exception,msg is:{}",e.getMessage());
        }finally {
            if (socket != null && socket.isConnected()){
                try {
                    socket.close();
                } catch (Exception e) {
                    log.error("closeSocketConnect method IOException,msg is:{}",e.getMessage());
                }
            }
        }
    }

    /**
     *  发送心跳
     *  1. 检测心跳时间是否到了
     *      1.时间到了，发送心跳
     *      2.检测心跳结果，结果正常，则更新上次心跳时间
     *      3.心跳不正常，则不更新时间
     *  2. 时间没有到，则不操作
     */
    public void checkAndSendHeartBeat() {
        log.info("checkAndSendHeartBeat ....");
        Date date = new Date();
        if (lastHeartBeatTime == null){
            lastHeartBeatTime = new Date();
        }
        int interval = DateUtil.compareTime(date, lastHeartBeatTime);
        log.info("interval time is :"+interval);
        if (interval >= OMCSocketMsgHeaders.HeartBeatInterval){
            try{
                SocketPacket heartBeatPacket = OMCSocketMsgHeaders.configHeartBeatPacket(getReqId());
                if (log.isDebugEnabled()){
                    log.debug("heartBeatPacket :{}",heartBeatPacket.getBody());
                }
                Socketutil.sendSocket(heartBeatPacket,socket);
                log.info("send omc heartbeat...");
            } catch (Exception e) {
                updateErrorTimes(e);
                log.error("checkAndSendHeartBeat method error,err msg is:{}",e.getMessage());
            }
        }
    }
    /**
     *  1.把接收到的信息  封装到OMCAlarmbean 和 po bean中(入库使用)
     *  2.把告警信息  发送kafka
     * @param msg
     */
    public void configOMCMsgToBean(String msg) throws Exception{
        String uUid = UUID.randomUUID().toString().replaceAll("-", "");
        log.debug("configOMCMsgTOBean, uuid:{}",uUid);
        /*String formatAlarmMsg = vpimReceiverService.formatAlarmInfoToFM(sourceBo.getSourceType(), sourceBo, msg, uUid);
        kafkaProducer.sendMsg(nfvConfig.getAlarmTopic(),sourceBo.getSourceId(),formatAlarmMsg);
        redisService.incrKeyForFMTrafficStatistics();

        OMCAlarmBean omcAlarmBean = JSONUtil.jsonToBean(msg, OMCAlarmBean.class);
        omcAlarmBean.setVenderID(sourceBo.getVendorId());
        omcAlarmBean.setSourceID(sourceBo.getSourceId());
        OrigAlarm origAlarm = new OrigAlarm();
        origAlarm.setAlarmId(omcAlarmBean.getAlarmId());
        origAlarm.setAlarmSeq(omcAlarmBean.getAlarmSeq().longValue());
        origAlarm.setCollectTimestamp(new Date());
        origAlarm.setUuid(uUid);
        origAlarm.setVendorId(omcAlarmBean.getVenderID());
        origAlarm.setSourceId(omcAlarmBean.getSourceID());
        origAlarm.setAlarmContent(msg.replaceAll("\u0000", ""));
        origAlarm.setNewAlarmId(JSON.parseObject(formatAlarmMsg).getString("alarmId"));
        emsAlarms.add(origAlarm);
        currentAlarmSeq = omcAlarmBean.getAlarmSeq();
        // 入库
        int time = DateUtil.compareTime(currentReadTime, lastReadTime);
        updateMonitorDB(time);*/
    }

    private void updateMonitorDB(int time){
        log.debug("updateMonitorDB");
        /*Integer sizeThreshold = nfvConfig.getEmsSaveSizeThreshold();
        Integer timeThreshold = nfvConfig.getEmsSaveTimeThreshold();
        if (emsAlarms.size() >= sizeThreshold || time >= timeThreshold){
            executorService.submit(this.new updateDB(emsAlarms));
            emsAlarms = new ArrayList<>();
        }*/
    }


    private int updateSourceMonitor(){
        int count = 0;
        /*String sourceId = taskMapper.checkSource(sourceMonitor);
        if (StringUtils.isBlank(sourceId)) {
            count = taskMapper.insertMonitorRecord(sourceMonitor);
        } else {
            count = taskMapper.updateAlarmSeq(sourceMonitor.getAlarmSeq(),sourceMonitor.getUpdateTime(),sourceMonitor.getHeartbeatTime(),
                    sourceMonitor.getSourceId());
        }*/
        return count;
    }

    public void setReqId(Integer reqId) {
        this.reqId.set(reqId);
    }

    /**
     * 获取操作序号
     */
    public static int getReqId() {
        int value;
        if (reqId.get() <0 || reqId.get() == Integer.MAX_VALUE){
            value = reqId.getAndSet(1);
        }else{
            value = reqId.getAndAdd(1);
        }
        return value;
    }
    /**
     *  分析登录的结果
     *  结果: ackLoginAlarm;result=fail;resDesc=username-error
     * @param response
     * @return
     */
    private boolean analysisLoginResult(String response){
        if (response == null || response.length() == 0){
            return false;
        }
        String[] split = response.split(";");
        String res = split[1].split("=")[1].toLowerCase();
        if (res.trim().equals("fail")){
            return false;
        }
        if (res.trim().equals("succ")){
            return true;
        }
        return false;
    }

    class updateDB implements Runnable{
        private List<OrigAlarm> origAlarmList;
        public updateDB(List<OrigAlarm> origAlarmList) {
            this.origAlarmList = origAlarmList;
        }
        @Override
        public void run() {
            //alarmMapper.insertEmsAlarmByBatch(origAlarmList);
            //origAlarmList = new ArrayList<>();
        }
    }

    class updateMonitor implements Runnable{
        @Override
        public void run() {
            log.info("update heartbeat , update collectionSourceMonitor...");
           /* CollectionSourceMonitor collectionSourceMonitor = new CollectionSourceMonitor(sourceBo.getSourceId(), null, "FM",
                    null, new Date(), new Date());
            String sourceId = taskMapper.checkSource(collectionSourceMonitor);
            if (StringUtils.isBlank(sourceId)) {
                taskMapper.insertMonitorRecord(collectionSourceMonitor);
            } else {
                taskMapper.updateHeartbeatTime(collectionSourceMonitor);
            }*/
        }
    }

    class updateSeqToDB implements Runnable{
        private String sourceId;
        private Integer alarmSeq;
        public updateSeqToDB(String sourceId,Integer alarmSeq) {
            this.sourceId = sourceId;
            this.alarmSeq = alarmSeq;
        }
        @Override
        public void run() {
            log.info("updateAlarmSeqToDB...");
            /*CollectionSourceMonitor sourceMonitor = new CollectionSourceMonitor(sourceId, null, "FM",
                    alarmSeq, new Date(), new Date());
            updateSourceMonitor(sourceMonitor);*/
        }
    }

    private void updateAlarmSeqToDB(String sourceId,Integer alarmSeq) {
        log.debug("updateAlarmSeqToDB,sourceId:{},alarmSeq:{}",sourceId,alarmSeq);
        /*CollectionSourceMonitor sourceMonitor = new CollectionSourceMonitor(sourceId, null, "FM",
                alarmSeq, new Date(), new Date());
        updateSourceMonitor(sourceMonitor);*/
    }

    private void updateForSokcetHeartBeat() {
        log.info("update collectionSourceMonitor for socket heartbeat...");
        /*CollectionSourceMonitor collectionSourceMonitor = new CollectionSourceMonitor(sourceBo.getSourceId(), null, "FM",
                null, new Date(), new Date());
        String sourceId = taskMapper.checkSource(collectionSourceMonitor);
        if (StringUtils.isBlank(sourceId)) {
            taskMapper.insertMonitorRecord(collectionSourceMonitor);
        } else {
            taskMapper.updateHeartbeatTime(collectionSourceMonitor);
        }*/
    }

}
