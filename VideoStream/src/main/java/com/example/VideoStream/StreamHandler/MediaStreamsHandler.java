package com.example.VideoStream.StreamHandler;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class MediaStreamsHandler extends AbstractWebSocketHandler {
    int counter = 0;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        System.out.println("Connection established");

    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        super.handleBinaryMessage(session, message);

        String data = new String(message.getPayload().array());
//        System.out.println("binary" + message.getPayload().array().length);
        session.sendMessage(new BinaryMessage(data.getBytes()));
//        System.out.println("binary " + data);

//        InputStream inputStream = new FileInputStream(new File("videoData.txt"));/
        InputStream targetStream = new ByteArrayInputStream(message.getPayload().array());

        BufferedImage image = ImageIO.read(targetStream);

        // Save the image to a file using the ImageIO class
        File outputFile = new File("/target/nitin" + counter++ + ".jpg");
        ImageIO.write(image, "jpg", outputFile);

//        System.out.println("Image saved successfully.");

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);

        String data = new String(message.getPayload());
//        System.out.println("text " + Arrays.toString(message.asBytes()));
//        session.sendMessage(new BinaryMessage(data.getBytes()));

        System.out.println("text " + data);
        data = data.split(",")[1];

        InputStream targetStream = new ByteArrayInputStream(Base64.getDecoder().decode(data.getBytes()));

        BufferedImage image = ImageIO.read(targetStream);

        // Save the image to a file using the ImageIO class
        File outputFile = new File("./target/nitin" + counter++ + ".jpeg");
        ImageIO.write(image, "jpeg", outputFile);

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        System.out.println("Connection closed");

    }
}
