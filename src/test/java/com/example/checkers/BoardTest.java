package com.example.checkers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.net.*;
import java.io.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class BoardTest {
  CheckersBoard board;
  ArrayList<Piece> blackPieces;
  ArrayList<Piece> whitePieces;
  ServerSocket serverSocket;
  Game game;
  PrintWriter out1, out2;
  BufferedReader in1, in2;
  Socket firstClient, secondClient;

  void listenSocket1() throws IOException {
    Socket socket = new Socket("localhost", 1000);
    OutputStream outStream = socket.getOutputStream();
    out1 = new PrintWriter(outStream, true);
    InputStream inStream = socket.getInputStream();
    in1 = new BufferedReader(new InputStreamReader(inStream));
  }
  void listenSocket2() throws IOException {
    Socket socket = new Socket("localhost", 1000);
    OutputStream outStream = socket.getOutputStream();
    out2 = new PrintWriter(outStream, true);
    InputStream inStream = socket.getInputStream();
    in2 = new BufferedReader(new InputStreamReader(inStream));
  }
  @BeforeEach
  void startServer() {
    new Thread(() -> {
      try (ServerSocket serverSocket = new ServerSocket(1000)) {
        while ((true)) {
          firstClient = serverSocket.accept();
          secondClient = serverSocket.accept();
          game = new Game(firstClient, secondClient);
          Thread gameThread = new Thread(game);
          gameThread.start();
        }
      } catch (IOException e) {
        System.out.println("error");
      }
    }).start();
    try {
      listenSocket1();
      listenSocket2();
    } catch (IOException e) {}
  }

  @Test
  void sendMsgToServer() {
    board = new PolishBoard();
    board.sendMsgToServer(firstClient);
    board.sendMsgToServer(secondClient);
  }

  @Test
  public void testPolish() {
    board = new PolishBoard();
    board.createBoard();
    blackPieces = board.getBlackPieces();
    whitePieces = board.getWhitePieces();
    assertNotNull(board.getSquares());
    assertEquals(board.getSize(), 10);
    assertEquals(blackPieces.size(), 20);
    assertEquals(whitePieces.size(), 20);
  }

  @Test
  public void testClassic() {
    board = new ClassicBoard();
    board.createBoard();
    blackPieces = board.getBlackPieces();
    whitePieces = board.getWhitePieces();
    assertNotNull(board.getSquares());
    assertEquals(board.getSize(), 8);
    assertEquals(blackPieces.size(), 12);
    assertEquals(whitePieces.size(), 12);
  }
  @Test
  public void testRussian() {
    board = new RussianBoard();
    board.createBoard();
    blackPieces = board.getBlackPieces();
    whitePieces = board.getWhitePieces();
    assertNotNull(board.getSquares());
    assertEquals(board.getSize(), 8);
    assertEquals(blackPieces.size(), 12);
    assertEquals(whitePieces.size(), 12);
  }

}
