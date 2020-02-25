package com.wk.fcaps.socket.client;

import com.jcraft.jsch.JSchException;
import com.wk.fcaps.socket.bean.SourceBo;
import com.wk.fcaps.socket.constants.OMCSocketMsgHeaders;
import com.wk.fcaps.socket.constants.SocketPacket;
import com.wk.fcaps.socket.util.DateUtil;
import com.wk.fcaps.socket.util.Socketutil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.Socket;
import java.util.*;

/**
 * 1. 登录
 * 2. 发送文件同步请求
 * 3. 获取文件路径
 * 4. 下载文件
 */
@Slf4j
public class FileSyncTask extends Thread {

    private Integer alarmSeq;

    private Socket socket;

    private Integer syncSource;

    private SourceBo sourceBo;

//    private NfvConfig nfvConfig = NfvConfig.getConfig();

    private volatile boolean isRunning = true;

//    private NfvConfig config = NfvConfig.getConfig();

    private Date currentTime;

    private List<String> downLoadFiles = new ArrayList<>();

//    private KafkaProducer kafkaProducer;

//    private VpimReceiverService vpimReceiverService;

//    private AlarmMapper alarmMapper;

//    private RedisService redisService;

    public FileSyncTask(Integer alarmSeq,SourceBo sourceBo){
        this.alarmSeq = alarmSeq;
        this.sourceBo = sourceBo;
        this.currentTime = new Date();
//        this.kafkaProducer = kafkaProducer;
//        this.vpimReceiverService = vpimReceiverService;
//        this.alarmMapper = alarmMapper;
//        this.redisService = redisService;
    }

    @Override
    public void run() {
        try {
            socket = Socketutil.login(sourceBo.getSocketIp(), sourceBo.getSocketPort(), sourceBo.getSocketUser(),
                    sourceBo.getSocketPassword(), OMCSocketMsgHeaders.FTPType);
            SocketPacket fileSyncPacket = OMCSocketMsgHeaders.configFileSyncPacket(getReqId(), alarmSeq + 1, OMCSocketMsgHeaders.FileSyncSource1);
            log.info("socket file sync msg is: {}", fileSyncPacket.getBody());
            Socketutil.sendSocket(fileSyncPacket, socket);
            while (isRunning) {
                checkAndSendHeartBeat();
                String fileSyncResult = "";
                try {
                    fileSyncResult = Socketutil.receiveSocket(socket);
                    log.info("fileSyncResult is :" + fileSyncResult);
                    dealFileSyncResponse(fileSyncResult);
                } catch (IOException e) {
                    if (e.getMessage().equalsIgnoreCase("Read timed out") ||
                            e.getMessage().contains("Read")) {
                        log.debug("FileSyncTask IOException,error msg is:" + e.getMessage());
                    } else {
                        log.error("FileSyncTask method error,err msg is:{}", e.getMessage());
                        stopRunning();
                    }
                } catch (Exception e) {
                    log.error("FileSyncTask Exception,error msg is:" + e.getMessage());
                    stopRunning();
                }
            }
        } catch (IOException e) {
            log.error("FileSyncTask IOException,error msg is:"+e.getMessage());
        } catch (Exception e){
            log.error("FileSyncTask Exception,error msg is:"+e.getMessage());
        }finally {
            if (socket != null){
                try {
                    Socketutil.closeOMCConnect(socket);
                    log.info("close fileSyncTask connection..");
                } catch (IOException e) {
                    log.info("closeOMCConnect IOException,error msg is:"+e.getMessage());
                }
            }
            sendFileContentToKafka();
        }
    }

    public void dealFileSyncResponse(String msg) throws Exception {
        if (StringUtils.isNotEmpty(msg)) {
            if (msg.contains(OMCSocketMsgHeaders.ackSyncAlarmFileResult)) {
                String[] syncFilePaths = getFilePathFromFileSyncResponse(msg);
                if (syncFilePaths != null && syncFilePaths.length > 0) {
                    log.info("omc file sync,begin to download file");
                    downFile(syncFilePaths);
                    log.info("download file complete....");
                    stopRunning();
                }
            } else if (msg.contains(OMCSocketMsgHeaders.ackSyncAlarmFile)) {
                if (!checkFileSynFirstResult(msg)) {
                    isRunning = false;
                }
            }
        }
    }

    /**
     *  把同步的文件内容 发送到kakfa
     */
    private void sendFileContentToKafka() {
        /*if (downLoadFiles.size() <= 0){
            return;
        }
        String alarmTopic = config.getAlarmTopic();
        String sourceId = sourceBo.getSourceId();
        List<OrigAlarm> emsAlarms = new ArrayList<>();
        for (String filePath : downLoadFiles) {
            log.info("begin to send fileContent to kafka");
            String alarm;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)), "UTF-8"))) {
                while ((alarm = reader.readLine()) != null){
                    String uUID = UUID.randomUUID().toString().replace("-", "");
                    String formatAlarm = vpimReceiverService.formatAlarmInfoToFM(sourceBo.getSourceId(), sourceBo, alarm, uUID);
                    if (log.isDebugEnabled()){
                        log.debug("format alarm is:"+formatAlarm);
                    }

                    kafkaProducer.sendMsg(alarmTopic,sourceId,formatAlarm);
                    redisService.incrKeyForFMTrafficStatistics();

                    JSONObject al = JSONUtil.toJSONObjectLower(alarm);
                    OrigAlarm origAlarm = new OrigAlarm();
                    origAlarm.setUuid(uUID);
                    origAlarm.setAlarmContent(formatAlarm.replaceAll("\u0000", ""));
                    origAlarm.setAlarmId(al.getString("alarmid"));
                    origAlarm.setAlarmSeq(Long.valueOf(al.getInteger("alarmseq")));
                    origAlarm.setCollectTimestamp(new Date());
                    origAlarm.setSourceId(sourceId);
                    origAlarm.setVendorId(sourceBo.getVendorId());
                    origAlarm.setNewAlarmId(JSON.parseObject(formatAlarm).getString("alarmId"));
                    emsAlarms.add(origAlarm);
                }
                //分批处理数据
                List<Collection> collections = CommonUtil.splitList(emsAlarms, config.getAlarmBatchLimit());
                for (Collection collection : collections) {
                    List<OrigAlarm> batchOal = (List<OrigAlarm>)collection;
                    alarmMapper.insertEmsAlarmByBatch(batchOal);
                }
            } catch (FileNotFoundException e) {
                log.error("sendFileContentToKafka FileNotFoundException,msg is :"+e.getMessage());
            } catch (IOException e) {
                log.error("sendFileContentToKafka IOException,msg is :"+e.getMessage());
            } catch (Exception e){
                log.error("sendFileContentToKafka Exception, msg is :{} ",e.getMessage());
            }
        }*/
        log.info("sendFileContentToKafka done...");
    }

    public boolean checkFileSynFirstResult(String res){
        //String emsFileSyncKeyWord = config.getEmsFileSyncKeyWord();
        String emsFileSyncKeyWord = "";
        log.info("checkFileSynFirstResult emsFileSyncKeyWord is: "+emsFileSyncKeyWord);
        if (res.trim().contains(emsFileSyncKeyWord)){
            return true;
        }
        return false;
    }

    /**
     *  从文件同步的返回结果中,得到文件路径
     *  ackSyncAlarmFileResult;reqId=33;result=succ;
     *  filename=/ftproot/GD/WX/HW/JS_OMC2/FM/20150611/ FM-OMC-1A-V1.1.0-20150611011603-001.txt;resDesc=null
     * @return
     */
    private String[] getFilePathFromFileSyncResponse(String fileSyncRes) throws Exception {
        String emsFileSyncFilename = "";
        String[] filePath = null;
        if (fileSyncRes != null && fileSyncRes.length() > 0 && (fileSyncRes.contains(emsFileSyncFilename) || fileSyncRes.contains(emsFileSyncFilename.toLowerCase()))) {
            String[] split = fileSyncRes.split(";");
            String result = split[2].split("=")[1].toLowerCase();
            if (result.equals("succ")){
                String path = split[3].split("=")[1];
                log.info("Extract file path is:{}",path);
                if (path.contains(",")){
                    String[] split1 = path.split(",");
                    filePath = split1;
                }else {
                    filePath = new String[1];
                    filePath[0] = path;
                }
            }
        }else{
            throw new Exception("555555 getFilePathFromFileSyncResponse no fileName");
        }
        return filePath;
    }

    /**
     *  文件同步，下载文件
     */
    public void downFile(String[] syncFilePaths){
        String ftpType = sourceBo.getFtpOrSftp().toLowerCase();
        if (ftpType.equals("ftp")){
            try {
                ftpDownFile(syncFilePaths);
            } catch (IOException e) {
                log.error("DownFile method ftp error,msg is :{}",e.getMessage());
            }
        }else if(ftpType.equals("sftp")){
            try {
                sftpDownFile(syncFilePaths);
            } catch (JSchException e) {
                log.error("DownFile method sftp error,msg is :{}",e.getMessage());
            } catch (IOException e) {
                log.error("DownFile method sftp error,msg is :{}",e.getMessage());
            }
        }
    }

    public void ftpDownFile(String[] syncFilePaths) throws IOException {
  /*      FtpLogin ftpLogin = new FtpLogin(sourceBo.getFtpIp(), sourceBo.getFtpPort(), sourceBo.getFtpUser(), sourceBo.getFtpPassword());
        //FTPUtil.connect(ftpLogin);
        String emsId = sourceBo.getSourceId();
        String dataFormat = DateUtil.format(new Date(), "yyyyMMdd");
        String emsFmRepositoryPre = nfvConfig.getEmsFmRepository();
        log.info("download file to :"+emsFmRepositoryPre);
        String emsFmRepository = emsFmRepositoryPre  + emsId + "/" +dataFormat;
        log.info("final download file to :"+emsFmRepositoryPre);
        for (int i = 0; i < syncFilePaths.length;i++){
            String filePath = syncFilePaths[i];
            log.info("ftp begin to download file "+filePath);
            String parentPath = filePath.substring(0,filePath.lastIndexOf("/"));
            String fileName = filePath.substring(filePath.lastIndexOf("/")+1);
            Boolean downloadFlag = FTPUtil.downLoadfile(parentPath, fileName, emsFmRepository, ftpLogin);
            log.info("downloadFlag :{}",downloadFlag);
            String realPath = FileUtil.deCompressFile(emsFmRepository, fileName, true);
            downLoadFiles.add(realPath);
        }*/
        log.info("ftp download complete,downLoadFiles :{}",downLoadFiles);
    }

    public void sftpDownFile(String[] syncFilePaths) throws JSchException, IOException {
/*        FtpLogin ftpLogin = new FtpLogin(sourceBo.getFtpIp(), sourceBo.getFtpPort(), sourceBo.getFtpUser(), sourceBo.getFtpPassword());
        //SftpUtil.connect(ftpLogin);
        String emsId = sourceBo.getSourceId();
        String dataFormat = DateUtil.format(new Date(), "yyyyMMdd");
        String emsFmRepositoryPre = nfvConfig.getEmsFmRepository();
        log.info("download file to :"+emsFmRepositoryPre);
        String emsFmRepository = emsFmRepositoryPre + "/" + emsId + "/" +dataFormat +"/";
        log.info("final download file to :"+emsFmRepositoryPre);
        for (int i = 0; i < syncFilePaths.length;i++){
            String filePath = syncFilePaths[i];
            log.info("sftp begin to download file "+filePath);
            String parentPath = filePath.substring(0,filePath.lastIndexOf("/"));
            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
            SftpUtil.downLoad(parentPath,fileName,emsFmRepository+fileName,ftpLogin);
            String realPath = FileUtil.deCompressFile(emsFmRepository, fileName, true);
            downLoadFiles.add(realPath);
        }*/
        //SftpUtil.closeConnect();
        log.info("sftp download complete");
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
        log.info("fileSyncTask checkAndSendHeartBeat ....");
        Date date = new Date();
        int interval = DateUtil.compareTime(date, currentTime);
        log.info("interval time is :"+interval);
        if (interval >= OMCSocketMsgHeaders.HeartBeatInterval){
            try{
                SocketPacket heartBeatPacket = OMCSocketMsgHeaders.configHeartBeatPacket(getReqId());
                Socketutil.sendSocket(heartBeatPacket,socket);
                log.info("fileSyncTask send omc heartbeat...");
                String heartPacket = Socketutil.receiveSocket(socket);
                if (heartPacket != null && heartPacket.length() > 0){
                    currentTime = date;
                }
            } catch (IOException e) {
                log.error("FileSyncTask checkAndSendHeartBeat method error,err msg is:{}",e.getMessage());
            }
        }
    }

    public void stopRunning(){
        isRunning = false;
    }

    public int getReqId(){
        int reqId = SocketTask.getReqId();
        return reqId;
    }
}
