package com.wk.entity.fcaps;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class AmCollectorSource {
    private String sourceId;

    private String sourceName;

    private String sourceType;

    private String vendorId;

    private String costClass;

    private Integer collectorId;

    private Integer pmPeriod;

    private Integer cmFmHeartBeat;

    private String domainType;

    private String ftpOrSftp;

    private String ftpIp;

    private Integer ftpPort;

    private String ftpUser;

    private String ftpPassword;

    private String socketIp;

    private Integer socketPort;

    private String socketUser;

    private String socketPassword;

    private String state;

    private Date updateTime;

    private String vnfmEndpointUrl;

    private String vnfmUsername;

    private String vnfmPassword;

    @Override
    public String toString() {
        return "AmCollectorSource{" +
                "sourceId='" + sourceId + '\'' +
                ", sourceName='" + sourceName + '\'' +
                ", sourceType='" + sourceType + '\'' +
                ", vendorId='" + vendorId + '\'' +
                ", costClass='" + costClass + '\'' +
                ", collectorId=" + collectorId +
                ", pmPeriod=" + pmPeriod +
                ", cmFmHeartBeat=" + cmFmHeartBeat +
                ", domainType='" + domainType + '\'' +
                ", ftpOrSftp='" + ftpOrSftp + '\'' +
                ", ftpIp='" + ftpIp + '\'' +
                ", ftpPort=" + ftpPort +
                ", ftpUser='" + ftpUser + '\'' +
                ", ftpPassword='" + ftpPassword + '\'' +
                ", socketIp='" + socketIp + '\'' +
                ", socketPort=" + socketPort +
                ", socketUser='" + socketUser + '\'' +
                ", socketPassword='" + socketPassword + '\'' +
                ", state='" + state + '\'' +
                ", updateTime=" + updateTime +
                ", vnfmEndpointUrl='" + vnfmEndpointUrl + '\'' +
                ", vnfmUsername='" + vnfmUsername + '\'' +
                ", vnfmPassword='" + vnfmPassword + '\'' +
                '}';
    }
}