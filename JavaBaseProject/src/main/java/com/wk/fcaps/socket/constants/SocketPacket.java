package com.wk.fcaps.socket.constants;

import java.io.UnsupportedEncodingException;

public class SocketPacket {

    private Short lenOfBody;

    private Short msgType;

    private String body;

    /**
     *  消息头  消息体 转换为byte
     *  1. 开始字段
     *  2. 请求类型
     *  3. 从1970.1.1 00:00:00 到现在的秒数
     *  4. 请求体长度
     *  5. 消息体
     * @return
     * @throws UnsupportedEncodingException
     */
    public byte[] getByteStream() throws UnsupportedEncodingException {
        byte[] bodyBytes = this.body.getBytes("UTF-8");
        Integer bodyBytesLength = bodyBytes.length;
        Integer socketLenth = 9 + bodyBytesLength;

        byte[] socketBytes = new byte[socketLenth];

        int index = 3;
        // 1. 开始
        socketBytes[0] = (byte)0xFF;
        socketBytes[1] = (byte)0xFF;
        socketBytes[2] = (byte)(getMsgType().shortValue());
        // 3. 时间戳
        byte[] timeStamp = intT4bytes((int)(System.currentTimeMillis()/1000));
        for (int i = 0; i < timeStamp.length; i++) {
            socketBytes[index++] = timeStamp[i];
        }
        // 4.消息体长度
        byte[] bodylength = intT2bytes(this.lenOfBody);
        for (int i = 0; i < bodylength.length; i++) {
            socketBytes[index++] = bodylength[i];
        }

        // 5. 消息体
        for (int i = 0; i < bodyBytes.length; i++) {
            socketBytes[index++] = bodyBytes[i];
        }
        return socketBytes;
    }

    /**
     *  将int转换为高字节在前，低字节在后的byte数组
     *  Big-endian 模式
     * @param res
     * @return
     */
    public byte[] intT4bytes(int res){
        byte[] targets = new byte[4];
        targets[3] = (byte)(res & 0xFF);
        targets[2] = (byte)((res >> 8) & 0xFF);
        targets[1] = (byte)((res >> 16) & 0xFF);
        targets[0] = (byte)((res >>> 24) & 0xFF);
        return targets;
    }

    public byte[] intT2bytes(short res){
        byte[] targets = new byte[2];
        targets[1] = (byte)(res & 0xFF);
        targets[0] = (byte)((res >>> 8) & 0xFF);
        return targets;
    }

    public Short getLenOfBody() {
        return lenOfBody;
    }

    public void setLenOfBody(Short lenOfBody) {
        this.lenOfBody = lenOfBody;
    }

    public Short getMsgType() {
        return msgType;
    }

    public void setMsgType(Short msgType) {
        this.msgType = msgType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
