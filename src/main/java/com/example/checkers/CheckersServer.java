package com.example.checkers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import static java.lang.Boolean.parseBoolean;


/**
 * Class initializing the server which listens on specific port and waits for 2 clients to connect
 * Class for creating the game object with 2 connected to server clients and starting the thread for this class
 * @author Bartłomiej Puchała
 * @link{com.example.checkers.Game}
 */
public class CheckersServer {
  public static void main(String[] args) {
    try (ServerSocket serverSocket = new ServerSocket(4000)) {

      System.out.println("Server is listening on port 4000");

      while(true) {
        Socket firstClient = serverSocket.accept();
        System.out.println("First client connected!");
        InputStream input_first = firstClient.getInputStream();
        BufferedReader in_first = new BufferedReader(new InputStreamReader(input_first));
        String data = in_first.readLine();

        System.out.println(data);
        if (parseBoolean(data)) {
          System.out.println("Waiting for second player to connect");
          Socket secondClient = serverSocket.accept();
          System.out.println("Second client connected");
          Game game = new Game(firstClient, secondClient);
          Thread gameThread = new Thread(game);
          gameThread.start();
        } else {
          GameBot gameBot = new GameBot(firstClient);
          Thread gameThread = new Thread(gameBot);
          gameThread.start();
        }
      }

        // Walidacja: Tylko 2 klientów może się połączyć z serwerem

//      }

    } catch(IOException e) {
      System.out.println("I/O error: " + e);
    }
  }
}
