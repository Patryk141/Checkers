package com.example.checkers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CheckersServerTest {
    @BeforeEach
    void startServerTest() {
        String args[] = {""};
        CheckersServer server = new CheckersServer();
        new Thread(() -> {
            server.main(args);
        }).start();
    }
    @Test
    void connectingWithServer() {
        CheckersApp app;
        final CheckersBoard board = new PolishBoard();
        app = new CheckersApp();
        new Thread(() -> {
            try {
                app.initApp(board, null, false);
            } catch(Exception e) {
                System.out.println(e);
            }
        }).start();
        new Thread(() -> {
            try {
                app.initApp(board, null, false);
            } catch(Exception e) {
                System.out.println(e);
            }
        }).start();
    }
}