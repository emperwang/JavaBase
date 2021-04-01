package com.wk.dp.structural.adaptor;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 11:30
 * @Description
 */
// 把英式插座 转换为 中式插座
public class BritishToChineseAdaptor {
    private SocketEntry socketEntry;

    public BritishToChineseAdaptor(BritishSocket britishSocket){
        socketEntry = britishSocket;
    }

    public SocketEntry getSocket(){
        ChineseSocket socket1 = new ChineseSocket();
        socket1.setVoltage(socketEntry.getVoltage());
        socket1.setType("chinese");
        return socket1;
    }
}
