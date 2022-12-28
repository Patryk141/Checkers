package com.example.checkers;

import javafx.scene.input.MouseEvent;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Game implements Runnable {

  private Socket firstPlayer;
  private Socket secondPlayer;
  private boolean gameEnded = false;

  private final static int FIRST=1; // Białe
  private final static int SECOND=2; // Czarne
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

  public String check_moveCentre(Piece piece) {
    int newX = (int) ((piece.getCenterX() - piece.getCenterX()%CheckersApp.PieceSize)/CheckersApp.PieceSize);
    int newY = (int) ((piece.getCenterY() - piece.getCenterY()%CheckersApp.PieceSize)/CheckersApp.PieceSize);
    if((newX + newY)%2 == 1) {
      return "valid"; // move is valid
    }
    else{
      return "not valid"; // move is not valid
    }
  }

  @Override
  public void run() {
      try {
        OutputStream output_first = firstPlayer.getOutputStream();
        ObjectOutputStream objOutput_first = new ObjectOutputStream(output_first);
        PrintWriter out_first = new PrintWriter(output_first, true);

        OutputStream output_second = secondPlayer.getOutputStream();
        ObjectOutputStream objOutput_second = new ObjectOutputStream(output_second);
        PrintWriter out_second = new PrintWriter(output_second, true);

        InputStream input_first = firstPlayer.getInputStream();
        ObjectInputStream objInput_first = new ObjectInputStream(input_first);
        BufferedReader in_first = new BufferedReader(new InputStreamReader(input_first));

        InputStream input_second = secondPlayer.getInputStream();
        ObjectInputStream objInput_second = new ObjectInputStream(input_second);
        BufferedReader in_second = new BufferedReader(new InputStreamReader(input_second));


        out_first.println("1");
        out_second.println("2");

        String infoMove = ""; // String z jakimiś informacjami na temat przeprowadzanego ruchu

        do {

          if (turn == SECOND) { // Ruch czarnych
//            infoMove = in_second.readLine();

              Piece piece = (Piece)objInput_second.readObject();
              System.out.println(piece.getCenterX());
              System.out.println(piece);

              String response = check_moveCentre(piece);

              OutputStream out_check = secondPlayer.getOutputStream();
              PrintWriter out_check_writer = new PrintWriter(out_check, true);
              out_check_writer.println(response);

//            System.out.println(infoMove); // Wypisywanie na serwerze ruchu czarnych
            // Wysyłanie ruchu czarnych do klienta grającego białymi
            out_first.println("(" + response + ")");
            turn = FIRST;
          }

          if (turn == FIRST) { // Ruch białych

            Piece somePiece = (Piece)objInput_first.readObject();
            System.out.println(somePiece.getCenterX());
            System.out.println(somePiece);

            String response = check_moveCentre(somePiece);

            OutputStream out_check = firstPlayer.getOutputStream();
            PrintWriter out_check_writer = new PrintWriter(out_check, true);
            out_check_writer.println(response);

            System.out.println(infoMove); // Wypisywanie na serwerze ruchu białych
            // Wysyłanie ruchu białych do klienta grającego czarnymi
            out_second.println("(" + response + ")");
            turn = SECOND;
          }
        } while (!gameEnded);

      } catch (IOException ex) {
        System.err.println("ex");
      }
      catch(ClassNotFoundException e) {
        e.printStackTrace();
      }
  }

}
