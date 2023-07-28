package com.example.VideoStream.StreamHandler;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.lang.reflect.Array;
import java.util.Arrays;

public class MediaStreamsHandler extends AbstractWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        System.out.println("Connection established");

    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        super.handleBinaryMessage(session, message);

        String data = new String(message.getPayload().array());
        System.out.println("binary" + message.getPayload().array().length);
        session.sendMessage(new BinaryMessage(data.getBytes()));
//        System.out.println("binary " + data);

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);

        String data = new String(message.getPayload());
        System.out.println("text " + Arrays.toString(message.asBytes()));
//        session.sendMessage(new BinaryMessage(data.getBytes()));
        System.out.println("text " + data);

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        System.out.println("Connection closed");

    }
}
