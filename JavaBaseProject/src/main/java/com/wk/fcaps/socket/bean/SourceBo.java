package com.wk.fcaps.socket.bean;

import lombok.Data;
import lombok.ToString;

import java.util.Base64;
import java.util.List;

@Data
public class SourceBo {

    private String sourceId;
    private String sourceName;
    private String sourceType;
    private String vendorId;
    private Integer collectorId;
    private Integer pmPeriod;
    private Integer cmFmHeartBeat;
    private String ftpOrSftp;
    private String ftpIp;
    private Integer ftpPort;
    private String ftpUser;
    @ToString.Exclude
    private String ftpPassword;
    private String socketIp;
    private Integer socketPort;
    private String socketUser;
    @ToString.Exclude
    private String socketPassword;
    private String vnfmEndpointUrl;
    private String vnfmUsername;
    @ToString.Exclude
    private String vnfmPassword;
    /**
     * 采集任务列表
     */
    //private List<TaskTemplateBo> collectionTaskList;

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public Integer getCollectorId() {
        return collectorId;
    }

    public void setCollectorId(Integer collectorId) {
        this.collectorId = collectorId;
    }

    public Integer getPmPeriod() {
        return pmPeriod;
    }

    public void setPmPeriod(Integer pmPeriod) {
        this.pmPeriod = pmPeriod;
    }

    public Integer getCmFmHeartBeat() {
        return cmFmHeartBeat;
    }

    public void setCmFmHeartBeat(Integer cmFmHeartBeat) {
        this.cmFmHeartBeat = cmFmHeartBeat;
    }

    public String getFtpOrSftp() {
        return ftpOrSftp;
    }

    public void setFtpOrSftp(String ftpOrSftp) {
        this.ftpOrSftp = ftpOrSftp;
    }



    public Integer getFtpPort() {
        return ftpPort;
    }

    public void setFtpPort(Integer ftpPort) {
        this.ftpPort = ftpPort;
    }

    public String getFtpUser() {
        return ftpUser;
    }

    public void setFtpUser(String ftpUser) {
        this.ftpUser = ftpUser;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }


    public Integer getSocketPort() {
        return socketPort;
    }

    public void setSocketPort(Integer socketPort) {
        this.socketPort = socketPort;
    }

    public String getSocketUser() {
        return socketUser;
    }

    public void setSocketUser(String socketUser) {
        this.socketUser = socketUser;
    }

    public String getSocketPassword() {
        if (socketPassword == null) {
            return null;
        }
        return socketPassword;
    }

    public void setSocketPassword(String socketPassword) {
        this.socketPassword = socketPassword;
    }

/*    public List<TaskTemplateBo> getCollectionTaskList() {
        return collectionTaskList;
    }

    public void setCollectionTaskList(List<TaskTemplateBo> collectionTaskList) {
        this.collectionTaskList = collectionTaskList;
    }*/

    public String getFtpIp() {
        return ftpIp;
    }

    public void setFtpIp(String ftpIp) {
        this.ftpIp = ftpIp;
    }

    public String getSocketIp() {
        return socketIp;
    }

    public void setSocketIp(String socketIp) {
        this.socketIp = socketIp;
    }

    public SourceBo(String sourceId, String sourceName, String sourceType, String vendorId,
                    Integer collectorId, Integer pmPeriod, Integer cmFmHeartBeat,
                    String ftpOrSftp, String ftpIP, Integer ftpPort, String ftpUser,
                    String ftpPassword, String socketIP, Integer socketPort, String socketUser,
                    String socketPassword) {
        this.sourceId = sourceId;
        this.sourceName = sourceName;
        this.sourceType = sourceType;
        this.vendorId = vendorId;
        this.collectorId = collectorId;
        this.pmPeriod = pmPeriod;
        this.cmFmHeartBeat = cmFmHeartBeat;
        this.ftpOrSftp = ftpOrSftp;
        this.ftpIp = ftpIP;
        this.ftpPort = ftpPort;
        this.ftpUser = ftpUser;
        this.ftpPassword = ftpPassword;
        this.socketIp = socketIP;
        this.socketPort = socketPort;
        this.socketUser = socketUser;
        this.socketPassword = socketPassword;
        //this.collectionTaskList = collectionTaskList;
    }

    public SourceBo() {
    }
}
