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

  public boolean check_move(int oldX, int oldY, int newX, int newY) {
    if((newX + newY) % 2 == 1) {
      if(squares[newX][newY].getPiece() == null) {
        Piece newPiece = squares[oldX][oldY].getPiece();
        System.out.println(newPiece);
        newPiece.setCenterX(newX*CheckersApp.PieceSize+CheckersApp.PieceSize*0.5);
        newPiece.setCenterY(newY*CheckersApp.PieceSize+CheckersApp.PieceSize*0.5);
        newPiece.setX_pos(newX);
        newPiece.setY_pos(newY);
        newPiece.setOldX(newPiece.getCenterX() - newPiece.getCenterX() % CheckersApp.PieceSize + CheckersApp.PieceSize * 0.5);
        newPiece.setOldY(newPiece.getCenterY() - newPiece.getCenterY() % CheckersApp.PieceSize + CheckersApp.PieceSize * 0.5);
//        newPiece.
        squares[oldX][oldY].setPiece(null);
        squares[newX][newY].setPiece(newPiece);
        System.out.println(squares[newX][newY].getPiece());
        return true;
      }
    }
    return false;
  }

  public boolean checkIfMatted(int oldX, int oldY, int newX, int newY) {
    if(((oldX + newX) % 2 == 0) && ((oldY + newY) % 2 == 0)) {
      if(squares[(oldX + newX) / 2][(oldY + newY) / 2].getPiece().getPaint() != squares[newX][newY].getPiece().getPaint()) {
        // zwrócenie prawdy do klienta i usunięcie pośredniczącego pionka z planszy
        squares[(oldX + newX) / 2][(oldY + newY) / 2].setPiece(null);
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
        BufferedReader in_first = new BufferedReader(new InputStreamReader(input_first));

        InputStream input_second = secondPlayer.getInputStream();
        BufferedReader in_second = new BufferedReader(new InputStreamReader(input_second));

        OutputStream output_first = firstPlayer.getOutputStream();
        PrintWriter out_first = new PrintWriter(output_first, true);

        OutputStream output_second = secondPlayer.getOutputStream();
        PrintWriter out_second = new PrintWriter(output_second, true);

        out_first.println("1");
        out_second.println("2");


        do {
          if (turn == SECOND) { // Ruch czarnych
            line = in_second.readLine();
            String[] data_piece = line.split(" ");
            int oldX = parseInt(data_piece[0]);
            int oldY = parseInt(data_piece[1]);
            int newX = parseInt(data_piece[3]);
            int newY = parseInt(data_piece[4]);
            boolean checkMove = check_move(oldX, oldY, newX, newY);
            boolean checkMat = checkIfMatted(oldX, oldY, newX, newY);

            if(checkMove && checkMat) {
              out_second.println("valid " + line + " matted");
              System.out.println("valid " + line + " matted");
              out_first.println("valid " + line + " matted");
              System.out.println("move " + line + " matted");
              // Podwójne zbijanie (dodać itp)
              turn = FIRST;
            }

            else if(checkMove && !checkMat){
//              System.out.println(checkIfMatted(parseInt(data_piece[0]), parseInt(data_piece[1]), parseInt(data_piece[3]), parseInt(data_piece[4])));
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

          }
          if (turn == FIRST) { // Ruch białych
            //System.out.println("FIRST");
            line = in_first.readLine();
            String[] data_piece = line.split(" ");
            int oldX = parseInt(data_piece[0]);
            int oldY = parseInt(data_piece[1]);
            int newX = parseInt(data_piece[3]);
            int newY = parseInt(data_piece[4]);
            //PrintWriter out1 = new PrintWriter(firstPlayer.getOutputStream(), true);
//            System.out.println(( check_move(oldX, oldY, newX, newY) && !checkIfMatted(oldX, oldY, newX, newY) ));

            boolean checkMove = check_move(oldX, oldY, newX, newY);
            boolean checkMat = checkIfMatted(oldX, oldY, newX, newY);


            if(checkMove && checkMat) {
              System.out.println("Jestem tu w 1 ifie");
              out_first.println("valid " + line + " matted");
              System.out.println("valid " + line + " matted");
              out_second.println("valid " + line + " matted");
              System.out.println("move " + line + " matted");
              turn = SECOND;
            }
            else if(checkMove && !checkMat) {
//              System.out.println(checkIfMatted(parseInt(data_piece[0]), parseInt(data_piece[1]), parseInt(data_piece[3]), parseInt(data_piece[4])));
              out_first.println("valid " + line);
              System.out.println("valid " + line);
              out_second.println("valid " + line);
              System.out.println("move " + line);
              turn = SECOND;
            }
            else{
              out_first.println("not " + line);
              System.out.println("not " + line);
              out_second.println("not " + line);
              System.out.println("not " + line);
            }
            //String response = check_moveCentre(piece);
          }

        } while (!gameEnded);

      } catch (IOException ex) {
        System.err.println("ex");
      }
  }
}
