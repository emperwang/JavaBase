package com.wk.dp.structural.adaptor;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 11:27
 * @Description
 */
public class SocketEntry {
    // 插座类型
    private String type;
    // 插座电压
    private Integer voltage;

    public void setType(String type) {
        this.type = type;
    }

    public void setVoltage(Integer voltage) {
        this.voltage = voltage;
    }

    public Integer getVoltage() {
        return voltage;
    }

    public String getType() {
        return type;
    }
}
