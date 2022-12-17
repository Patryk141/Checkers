package com.example.checkers;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Game implements Runnable {

  private Socket firstPlayer;
  private Socket secondPlayer;
  private boolean gameEnded = false;

  private final static int FIRST=1; // Białe
  private final static int SECOND=2;
  private static int turn=FIRST;

  private List<Integer> availableMoves;

  public Game(Socket firstPlayer, Socket secondPlayer) {
    this.firstPlayer = firstPlayer;
    this.secondPlayer = secondPlayer;
  }

  public boolean isWinner() {

    return false;
  }

  public ArrayList<Integer> getAvailableMoves() {
    return (ArrayList<Integer>) availableMoves;
  }


  @Override
  public void run() {
    try {
      InputStream input_first = firstPlayer.getInputStream();
      BufferedReader in_first = new BufferedReader(new InputStreamReader(input_first));

      InputStream input_second = secondPlayer.getInputStream();
      BufferedReader in_second = new BufferedReader(new InputStreamReader(input_second));

      OutputStream output_first = firstPlayer.getOutputStream();
      PrintWriter out_first = new PrintWriter(output_first, true);

      OutputStream output_second = secondPlayer.getOutputStream();
      PrintWriter out_second = new PrintWriter(output_second, true);

      out_first.println("1");
      out_second.println("2");

      String infoMove; // String z jakimiś informacjami na temat przeprowadzanego ruchu

      do {
        if(turn == SECOND) { // Ruch czarnych
          // Odbieranie ruchu białych
          infoMove = in_second.readLine();
          System.out.println(infoMove);
          // Wysyłanie ruchu czarnych do klienta grającego białymi
          out_first.println("(" + infoMove + ")");
          turn = FIRST;
        }
        if(turn == FIRST) { // Ruch białych
          // Odbieranie ruchu czarnych
          infoMove = in_first.readLine();
          System.out.println(infoMove);
          // Wysyłanie ruchu białych do klienta grającego czarnymi
          out_second.println("(" + infoMove + ")");
          turn = SECOND;
        }
      } while(!gameEnded);

    }
    catch(IOException ex) {
      System.err.println("ex");
    }
  }

}
