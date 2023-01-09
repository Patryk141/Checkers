package com.example.checkers;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class CheckersAppTest {
  CheckersApp app;
  CheckersApp app2;
  CheckersServer server;
  CheckersBoard board;

  Piece pieceWhite1 = new Piece(3, 0, PieceType.WHITE);
  Piece pieceWhite2 = new Piece(2, 1, PieceType.WHITE);
  Piece pieceWhite3 = new Piece(4, 1, PieceType.WHITE);
  Piece pieceWhite4 = new Piece(0, 5, PieceType.WHITE);
  Piece pieceBlack1 = new Piece(1, 2, PieceType.BLACK);
  Piece pieceBlack2 = new Piece(3, 2, PieceType.BLACK);
  Piece pieceBlack3 = new Piece(3, 4, PieceType.BLACK);
  Piece pieceBlack4 = new Piece(1, 6, PieceType.BLACK);

  @BeforeEach
  void createBoard() {
    CheckersApp.squares = new Square[8][8];
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        CheckersApp.squares[i][j] = new Square(i, j, (i + j) % 2);
      }
    }
    CheckersApp.squares[3][0].setPiece(pieceWhite1);
    CheckersApp.squares[2][1].setPiece(pieceWhite2);
    CheckersApp.squares[4][1].setPiece(pieceWhite3);
    CheckersApp.squares[0][5].setPiece(pieceWhite4);
    CheckersApp.squares[1][2].setPiece(pieceBlack1);
    CheckersApp.squares[3][2].setPiece(pieceBlack2);
    CheckersApp.squares[3][4].setPiece(pieceBlack3);
    CheckersApp.squares[1][6].setPiece(pieceBlack4);
  }

  @BeforeEach
  public void setServer() {
    server = new CheckersServer();
    new Thread(()-> {
      server.main(new String[10]);
    }).start();
  }

  @Test
  public void testSocket() {
    app = new CheckersApp();
    CheckersApp app2 = new CheckersApp();
    app.listenSocket();
    app2.listenSocket();
    assertNotNull(app.socket);
    assertNotNull(app2.socket);
    assertNotNull(app.in);
    assertNotNull(app2.in);
    assertNotNull(app.out);
    assertNotNull(app2.out);
  }

  @Test
  public void testInfoFromServer() throws IOException {
    app = new CheckersApp();
    CheckersApp app2 = new CheckersApp();
    app.listenSocket();
    app2.listenSocket();
    app.receiveInfoFromServer();
    app2.receiveInfoFromServer();
    assertEquals(app.getPlayer(), 1);
    assertEquals(app2.getPlayer(), 2);
  }

  @Test
  public void testAttributes() {
//    CheckersBoard board;
    app = new CheckersApp();
    app2 = new CheckersApp();
    board = new PolishBoard();
    board.createBoard();
    new Thread(()-> {
      app.initApp(board, null);
    }).start();
    new Thread(()-> {
      app2.initApp(board, null);
    }).start();

    assertNotNull(app.PieceSize);
  }

  @Test
  public void changePositionTest() {
    app = new CheckersApp();
    String move = "valid 3 4 -> 2 3";
    Piece startPiece = pieceBlack3;
    app.changePosition(move);
    assertNull(CheckersApp.squares[3][4].getPiece());
    assertNotNull(CheckersApp.squares[2][3].getPiece());
  }

  @Test
  public void backPositionTest() {
    app = new CheckersApp();
    String move = "not 3 0 2 1";
    Piece oldPiece = CheckersApp.squares[3][0].getPiece();
    app.backPosition(move);
    assertEquals(oldPiece.getCenterX(), oldPiece.getOldX());
    assertEquals(oldPiece.getCenterY(), oldPiece.getOldY());
    assertNotEquals(oldPiece.getX_pos(), CheckersApp.squares[2][1].getPiece().getX_pos());
    assertNotEquals(oldPiece.getY_pos(), CheckersApp.squares[2][1].getPiece().getY_pos());
  }

  @Test
  public void promotionTest() {
    app = new CheckersApp();
    String move = "valid 0 5 -> 2 7 matted";
    app.changePosition(move);
    app.removePiece(move);
    assertNull(CheckersApp.squares[1][6].getPiece());
    assertNull(CheckersApp.squares[0][5].getPiece());
    assertNotNull(CheckersApp.squares[2][7].getPiece());
  }

}
