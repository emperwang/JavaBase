package com.wk.api.bean;

import java.io.Serializable;

public class User implements Serializable{
    private Integer id;  //用户名
    private String userAddress; //收获地址
    private String userId; //用户id
    private String consignee; //收货人
    private String phoneNum; //电话号码
    private String idDefault; //是否是默认地址

    public User() {
    }

    public User(Integer id, String userAddress, String userId, String consignee, String phoneNum, String idDefault) {
        this.id = id;
        this.userAddress = userAddress;
        this.userId = userId;
        this.consignee = consignee;
        this.phoneNum = phoneNum;
        this.idDefault = idDefault;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getIdDefault() {
        return idDefault;
    }

    public void setIdDefault(String idDefault) {
        this.idDefault = idDefault;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userAddress='" + userAddress + '\'' +
                ", userId='" + userId + '\'' +
                ", consignee='" + consignee + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", idDefault='" + idDefault + '\'' +
                '}';
    }
}
