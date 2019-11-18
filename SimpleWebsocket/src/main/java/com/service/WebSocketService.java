package com.service;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@ServerEndpoint("/simplews")
public class WebSocketService {

    public AtomicLong count = new AtomicLong(0);

    public Set<WebSocketService> sets = new HashSet<>();

    public Session session;

    @OnOpen
    public void onOpen(Session session){
        System.out.println("login ... ");
        this.session = session;
        count.getAndIncrement();
        sets.add(this);
    }

    @OnMessage
    public void onMessage(String message,Session session) throws IOException {
        System.out.println("receive msg is :"+message);
        for (WebSocketService item : sets) {
            item.sendMessage(message);
        }
    }

    private void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    @OnError
    public void OnError(Throwable e){
        System.out.println("error : msg = "+e.getMessage());
    }

    @OnClose
    public void close(){
        System.out.println("logout ... ");
        sets.remove(this);
        count.getAndDecrement();
        sets.add(this);
    }
}
