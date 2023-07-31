package com.example.VideoStream.StreamHandler;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class TCPSocketServer {

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @PostConstruct
    public void init() {
        executorService.submit(() -> startServer());
    }

    private void startServer() {
        try (ServerSocket socketServer = new ServerSocket(8090)) {
            System.out.println("TCP Socket Server started on port 8090");
            while (true) {
                Socket clientSocket = socketServer.accept();
                executorService.submit(() -> handlerClient(clientSocket));
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void handlerClient(Socket clientSocket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Receive message from client: " + inputLine);
                out.println("Server response: " + inputLine);
            }

            in.close();
            out.close();
            clientSocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

}
