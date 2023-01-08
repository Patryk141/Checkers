package com.example.checkers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    Game game;
    PrintWriter out1, out2;
    BufferedReader in1, in2;
    Socket firstClient, secondClient;
    void listenSocket1() throws IOException {
        Socket socket = new Socket("localhost", 1000);
        OutputStream outStream = socket.getOutputStream();
        out1 = new PrintWriter(outStream, true);
        InputStream inStream = socket.getInputStream();
        in1 = new BufferedReader(new InputStreamReader(inStream));
    }
    void listenSocket2() throws IOException {
        Socket socket = new Socket("localhost", 1000);
        OutputStream outStream = socket.getOutputStream();
        out2 = new PrintWriter(outStream, true);
        InputStream inStream = socket.getInputStream();
        in2 = new BufferedReader(new InputStreamReader(inStream));
    }
    @BeforeEach
    void startServer() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(1000)) {
                while ((true)) {
                    firstClient = serverSocket.accept();
                    secondClient = serverSocket.accept();
                    game = new Game(firstClient, secondClient);
                    Thread gameThread = new Thread(game);
                    gameThread.start();
                }
            } catch (IOException e) {
                System.out.println("error");
            }
        }).start();
        try {
            listenSocket1();
            listenSocket2();
        } catch (IOException e) {}
    }

    @Test
    void sendingToServer() throws IOException {
        out1.println("8 Russian");
        out2.println("8 Russian");
        BufferedReader in_first = new BufferedReader(new InputStreamReader(firstClient.getInputStream()));
        PrintWriter out_first = new PrintWriter(firstClient.getOutputStream(), true);
        PrintWriter out_second = new PrintWriter(secondClient.getOutputStream(), true);
        game.gameType = "Russian";
        game.rules = new RussianGameRules();
        game.createBoard();
        out1.println("7 2 -> 6 3");
        game.generateResponse(in_first , out_first, out_second, 2);
    }
}