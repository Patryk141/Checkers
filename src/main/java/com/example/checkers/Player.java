package com.example.checkers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Player implements Runnable {

  Socket socket = null;
  PrintWriter out = null;
  BufferedReader in = null;
  private int player;
  private Thread thread;
  private Player playerObj;
  public final static int PLAYER1 = 1;
  public final static int PLAYER2 = 2;
  public static int ACTIVE = 0;
  public static final int NONACTIVE = 1;
  private static int currentPlayer = PLAYER1; // ustawienie obecnego gracza na gracza, który zaczyna białymi

  private static int showing = ACTIVE;

  public Player() {
    this.listenSocket();
    this.receiveInfoFromServer();
  }

  @Override
  public void run() {
    playerMethod(player);
  }

  private void listenSocket() {
    try {
      System.out.println("Siema Eniu");
      socket = new Socket("localhost", 3000);
      // Inicjalizacja wysyłania do serwera
      out = new PrintWriter(socket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
    catch(IOException ex) {
      System.out.println("No I/O");
      System.exit(1);
    }
  }

  public void playerMethod(int player) {
    while(true) {
      synchronized(this) {
        if (currentPlayer == PLAYER1) {
          try {
            wait(10);
          }
          catch(InterruptedException e) {
            System.out.println(e);
          }
        }
        if(showing == ACTIVE) {
          receiveMove();
          showing = NONACTIVE;
        }
        notifyAll();
      }
    }
  }

  private void receiveMove() {
    try {
      // Odebranie z serwera informacji o ruchu białych
      String moveFromWHITE = in.readLine();
    } catch(IOException e) {
      System.out.println("Read failed");
      System.exit(1);
    }
  }

  private void receiveInfoFromServer() {
    try {

      player = Integer.parseInt(in.readLine()); // ustawienie
      System.out.println("Witam z tej strony Player " + player);
    }
    catch(IOException e) {
      System.out.println("Read Failed");
      System.exit(1);
    }
  }

}
