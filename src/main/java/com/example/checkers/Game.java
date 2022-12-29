package com.example.checkers;

import javafx.scene.input.MouseEvent;

import java.io.*;
import java.net.Socket;

import static java.lang.Integer.parseInt;

public class Game extends CheckersApp implements Runnable {

  private Socket firstPlayer;
  private Socket secondPlayer;
  private boolean gameEnded = false;

  private final static int FIRST=1; // Białe
  private final static int SECOND=2; // Czarne
  private static int turn=FIRST;
  String line;
  private Square[][] squares = new Square[Size][Size];
  public Game(Socket firstPlayer, Socket secondPlayer) {
    this.firstPlayer = firstPlayer;
    this.secondPlayer = secondPlayer;
  }

  public boolean isWinner() {
    return false;
  }

  /*
  public String check_moveCentre(Piece piece) {
    int newX = (int) ((piece.getCenterX() - piece.getCenterX()%CheckersApp.PieceSize)/CheckersApp.PieceSize);
    int newY = (int) ((piece.getCenterY() - piece.getCenterY()%CheckersApp.PieceSize)/CheckersApp.PieceSize);
    if((newX + newY)%2 == 1) {
      return "valid"; // move is valid
    }
    else{
      return "not_valid"; // move is not valid
    }
  }
*/
  public void createBoard(){
    for(i=0 ; i<Size ; i++) {
      for(j=0 ; j<Size ; j++){
        squares[i][j] = new Square(i,j,(i+j)%2);
        if((i+j)%2 != 0) {
          if(Size-4 < j){
            piece = new Piece(i,j,PieceType.BLACK);
            squares[i][j].setPiece(piece);
          }
          else if(3 > j){
            piece = new Piece(i,j,PieceType.WHITE);
            squares[i][j].setPiece(piece);
          }
        }
      }
    }
  }

  public boolean check_move(int oldX, int oldY, int newX, int newY){
    if((newX + newY)%2 == 1){
      if(squares[newX][newY].getPiece() == null){
        Piece newPiece = squares[oldX][oldY].getPiece();
        squares[oldX][oldY].setPiece(null);
        squares[newX][newY].setPiece(newPiece);
        return true;
      }
    }
    return false;
  }

  @Override
  public void run() {
    createBoard();
      try {
        InputStream input_first = firstPlayer.getInputStream();
        //ObjectInputStream objInput_first = new ObjectInputStream(input_first);
        BufferedReader in_first = new BufferedReader(new InputStreamReader(input_first));

        InputStream input_second = secondPlayer.getInputStream();
        //ObjectInputStream objInput_second = new ObjectInputStream(input_second);
        BufferedReader in_second = new BufferedReader(new InputStreamReader(input_second));

        OutputStream output_first = firstPlayer.getOutputStream();
        //ObjectOutputStream objOutput_first = new ObjectOutputStream(output_first);
        PrintWriter out_first = new PrintWriter(output_first, true);

        OutputStream output_second = secondPlayer.getOutputStream();
        //ObjectOutputStream objOutput_second = new ObjectOutputStream(output_second);
        PrintWriter out_second = new PrintWriter(output_second, true);

        out_first.println("1");
        out_second.println("2");


        do {
          if (turn == SECOND) { // Ruch czarnych
            //System.out.println("SECOND");;
            line = in_second.readLine();
            String[] data_piece = line.split(" ");
            PieceType type;
            //String response = check_moveCentre(piece);
            //PrintWriter out2 = new PrintWriter(secondPlayer.getOutputStream(), true);
            if(check_move(parseInt(data_piece[0]), parseInt(data_piece[1]), parseInt(data_piece[3]), parseInt(data_piece[4]))){
              out_second.println("valid " + line);
              System.out.println("valid " + line);
              out_first.println("valid " + line);
              System.out.println("move " + line);
              turn = FIRST;
            } else{
              out_second.println("not " + line);
              System.out.println("not " + line);
              out_first.println("not " + line);
              System.out.println("not " + line);
            }
            //out_first.println("valid");
            //out_second.println("valid");
            //System.out.println("tez juz");
          }
          if (turn == FIRST) { // Ruch białych
            //System.out.println("FIRST");
            line = in_first.readLine();
            String[] data_piece = line.split(" ");
            PieceType type;
            //PrintWriter out1 = new PrintWriter(firstPlayer.getOutputStream(), true);
            if(check_move(parseInt(data_piece[0]), parseInt(data_piece[1]), parseInt(data_piece[3]), parseInt(data_piece[4]))){
              out_first.println("valid " + line);
              System.out.println("valid " + line);
              out_second.println("valid " + line);
              System.out.println("move " + line);
              turn = SECOND;
            } else{
              out_first.println("not " + line);
              System.out.println("not " + line);
              out_second.println("not " + line);
              System.out.println("not " + line);
            }
            //String response = check_moveCentre(piece);
            //System.out.println("tez juz");
            //out_second.println("valid");
          }

        } while (!gameEnded);

      } catch (IOException ex) {
        System.err.println("ex");
      }
  }
}
