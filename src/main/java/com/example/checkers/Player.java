package com.example.checkers;

import javafx.application.Platform;
import javafx.scene.input.MouseEvent;

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
  MouseEvent e;
  Piece piece;
  int x_pos, y_pos;

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
    System.out.println(piece);
    if(e != null && piece != null) {
        Platform.runLater(() -> {
          newRunnable(e, piece, x_pos, y_pos);
        });
    }
    playerMethod(player);
  }

  private Runnable newRunnable(MouseEvent e, Piece piece, int x_pos, double y_pos) {
    Runnable updater = new Runnable() {
      @Override
      public void run() {
        double dx = e.getX() - piece.getCenterX();
        double dy = e.getY() - piece.getCenterY();
        if(piece.isHit(piece.getCenterX(), piece.getCenterY())) {
          piece.setCenterX(piece.getCenterX() + dx);
          piece.setCenterY(piece.getCenterY() + dy);
        }
      }
    };
    return updater;
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
        if (currentPlayer == player) {
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

  public void sendMove(MouseEvent e, Piece piece, int x_pos, int y_pos) {
//    System.out.println("Wykonałem ruch na playerze " + player);
      this.e = e;
      this.piece = piece;
      this.x_pos = x_pos;
      this.y_pos = y_pos;
      newRunnable(e, piece, x_pos, y_pos);
//    double dx = e.getX() - piece.getCenterX();
//    double dy = e.getY() - piece.getCenterY();
//    if(piece.isHit(piece.getCenterX(), piece.getCenterY())) {
//      piece.setCenterX(piece.getCenterX() + dx);
//      piece.setCenterY(piece.getCenterY() + dy);
//    }
//    piece.setCenterX(piece.getCenterX() + dx);
//    piece.setCenterY(piece.getCenterY() + dy);
    out.println(x_pos);
//    piece.. += dx;
//    y += dy;
  }

  private void receiveMove() {
    try {
      // Odebranie z serwera informacji o ruchu białych
      String move = in.readLine();
    } catch(IOException e) {
      System.out.println("Read failed");
      System.exit(1);
    }
  }

  private void receiveInfoFromServer() {
    try {
      player = Integer.parseInt(in.readLine()); // ustawienie
      System.out.println("Witam z tej strony Player " + player);
    } catch (IOException e) {
      System.out.println("Read Failed");
      System.exit(1);
    }
  }

}
