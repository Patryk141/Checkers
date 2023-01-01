package com.example.checkers;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

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

  // if piece can be mat
  private void mandatoryMove() {

  }

  private void updatePiece(Piece newPiece, int newX, int newY) {
    newPiece.setCenterX(newX * CheckersApp.PieceSize + CheckersApp.PieceSize * 0.5);
    newPiece.setCenterY(newY * CheckersApp.PieceSize + CheckersApp.PieceSize * 0.5);
    newPiece.setX_pos(newX);
    newPiece.setY_pos(newY);
    newPiece.setOldX(newPiece.getCenterX() - newPiece.getCenterX() % CheckersApp.PieceSize + CheckersApp.PieceSize * 0.5);
    newPiece.setOldY(newPiece.getCenterY() - newPiece.getCenterY() % CheckersApp.PieceSize + CheckersApp.PieceSize * 0.5);
    squares[newX][newY].setPiece(newPiece);
  }

  private boolean checkNormalMove(int oldX, int oldY, int newX, int newY) {
    if((newX + newY) % 2 == 1) {
      Piece newPiece = squares[oldX][oldY].getPiece();
      if(Math.abs(oldX - newX) == 1 && squares[newX][newY].getPiece() == null) {
        // validation for not moving the piece back
          if((newY - oldY) == 1 && newPiece.getPaint() == Color.WHITE) {
            updatePiece(newPiece, newX, newY);
            squares[oldX][oldY].setPiece(null);
            return true;
          }
          else if((oldY - newY) == 1 && newPiece.getPaint() == Color.BLACK) {
            updatePiece(newPiece, newX, newY);
            squares[oldX][oldY].setPiece(null);
            return true;
          }
      }
    }
    return false;
  }


  public boolean checkIfMatted(int oldX, int oldY, int newX, int newY) {
    // Pieces can either mat other by moving up or down
//    if(((oldX + newX) % 2 == 0) && ((oldY + newY) % 2 == 0))
    int neighbourX = (oldX + newX) / 2;
    int neighbourY = (oldY + newY) / 2;
    if(Math.abs(oldX - newX) == 2 && Math.abs(oldY - newY) == 2) {
      if(squares[neighbourX][neighbourY].getPiece() != null) {
        if (squares[neighbourX][neighbourY].getPiece().getPaint() != squares[oldX][oldY].getPiece().getPaint()) {
          // zwrócenie prawdy do klienta i usunięcie pośredniczącego pionka z planszy
          Piece newPiece = squares[oldX][oldY].getPiece();
          updatePiece(newPiece, newX, newY);
          squares[oldX][oldY].setPiece(null);
          squares[(oldX + newX) / 2][(oldY + newY) / 2].setPiece(null);
          return true;
        }
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
            boolean checkNormalMove = checkNormalMove(oldX, oldY, newX, newY);
            boolean checkMat = checkIfMatted(oldX, oldY, newX, newY);

            if(checkMat) {
              out_second.println("valid " + line + " matted");
              System.out.println("valid " + line + " matted");
              out_first.println("valid " + line + " matted");
              System.out.println("move " + line + " matted");
              // Podwójne zbijanie (dodać itp)
              turn = FIRST;
            }

            else if(checkNormalMove){
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
            line = in_first.readLine();
            String[] data_piece = line.split(" ");
            int oldX = parseInt(data_piece[0]);
            int oldY = parseInt(data_piece[1]);
            int newX = parseInt(data_piece[3]);
            int newY = parseInt(data_piece[4]);

            boolean checkNormalMove = checkNormalMove(oldX, oldY, newX, newY);
            boolean checkMat = checkIfMatted(oldX, oldY, newX, newY);

            if(checkMat) {
              out_first.println("valid " + line + " matted");
              System.out.println("valid " + line + " matted");
              out_second.println("valid " + line + " matted");
              System.out.println("move " + line + " matted");
              turn = SECOND;
            }
            else if(checkNormalMove) {
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
          }

        } while (!gameEnded);

      } catch (IOException ex) {
        System.err.println("ex");
      }
  }
}
