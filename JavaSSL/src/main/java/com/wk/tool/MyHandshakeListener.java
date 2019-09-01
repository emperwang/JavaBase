package com.wk.tool;

import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;

public class MyHandshakeListener implements HandshakeCompletedListener {
    @Override
    public void handshakeCompleted(HandshakeCompletedEvent handshakeCompletedEvent) {
        System.out.println("Handshake finished successfully");
    }
}
