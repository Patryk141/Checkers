package com.example.checkers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class CheckersServer {

  public static void main(String[] args) {
    try (ServerSocket serverSocket = new ServerSocket(4000)) {

      System.out.println("Server is listening on port 4000");

      while(true) {
        Socket firstClient = serverSocket.accept();
        System.out.println("First client connected!");
        System.out.println("Waiting for second player to connect");

        Socket secondClient = serverSocket.accept();
        System.out.println("Second client connected");

        Game game = new Game(firstClient, secondClient);
        Thread gameThread = new Thread(game);
        gameThread.start();

        // Walidacja: Tylko 2 klientów może się połączyć z serwerem

      }

    } catch(IOException e) {
      System.out.println("I/O error: " + e);
    }
  }
}
