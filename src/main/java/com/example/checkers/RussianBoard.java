package com.example.checkers;

import javafx.scene.Group;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class RussianBoard implements CheckersBoard {

  private static int Size = 8;
  private Square[][] squares = null;
  private ArrayList<Piece> blackPieces = new ArrayList<Piece>();
  private ArrayList<Piece> whitePieces = new ArrayList<Piece>();
  private int i, j;
  private Piece piece;

  @Override
  public Pane createBoard() {
    Pane root = new Pane();
    squares = new Square[Size][Size];
    Group squareGroup = new Group();
    Group pieceGroup = new Group();
    root.setPrefSize(Size*PieceSize,Size*PieceSize);
    for(i=0 ; i<Size ; i++) {
      for(j=0 ; j<Size ; j++){
        squares[i][j] = new Square(i,j,(i+j)%2);
        if((i+j)%2 != 0) {
          if(Size-4 < j){
            piece = new Piece(i,j,PieceType.BLACK);
            squares[i][j].setPiece(piece); // ustawienie na kwadratach pionków
            this.blackPieces.add(piece);
            pieceGroup.getChildren().add(piece);
          }
          else if(3 > j){
            piece = new Piece(i,j,PieceType.WHITE);
            squares[i][j].setPiece(piece);
            this.whitePieces.add(piece);
            pieceGroup.getChildren().add(piece);
          }

        }
        squareGroup.getChildren().add(squares[i][j]);
      }
    }

    root.getChildren().addAll(squareGroup,pieceGroup);

    return root;
  }

  @Override
  public void sendMsgToServer(Socket socket) {
    try {
      PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
      writer.println(Size + " " + "Russian");
    } catch(IOException err) {
      System.out.println(err);
    }
  }

  @Override
  public Square[][] getSquares() {
    return squares;
  }

  @Override
  public ArrayList<Piece> getBlackPieces() {
    return blackPieces;
  }

  @Override
  public ArrayList<Piece> getWhitePieces() {
    return whitePieces;
  }

  @Override
  public int getPieceSize() {
    return PieceSize;
  }

  @Override
  public int getSize() {
    return Size;
  }
}
