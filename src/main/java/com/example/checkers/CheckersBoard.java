package com.example.checkers;

import javafx.scene.layout.Pane;

import java.net.Socket;
import java.util.ArrayList;

/**
 * Interface for the each board
 * @author Bartłomiej Puchała Patryk Piskorski
 */
public interface CheckersBoard {
  public static final int PieceSize = 80;

  Pane createBoard();
  void sendMsgToServer(Socket socket);
  Square[][] getSquares();
  ArrayList<Piece> getBlackPieces();
  ArrayList<Piece> getWhitePieces();
  int getPieceSize();
  int getSize();

}
