package com.example.checkers;

import javafx.scene.input.MouseEvent;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

/**
 * Server Class for sending the response to the clients
 * @author Patryk Piskorski Bartłomiej Puchała
 */
public class Game extends CheckersApp implements Runnable {

  private Socket firstPlayer;
  private Socket secondPlayer;
  private boolean gameEnded = false;
  public static int size;
  private final static int FIRST=1; // Białe
  private final static int SECOND=2; // Czarne
  private static int turn=FIRST;
  private boolean checkMoves = false;
  private boolean checkMat = false;
  private boolean lock = true;
  private String line;
  private Square[][] squares;
  public String gameType;
  public GameRules rules = null;
  int blackPieces, whitePieces = 0;

  public Game(Socket firstPlayer, Socket secondPlayer) {
    this.firstPlayer = firstPlayer;
    this.secondPlayer = secondPlayer;
  }

  /**
   * Method for setting the squares object and initializing the GameRules instance
   */
  public void createBoard() {
    squares = new Square[size][size];

    for (int i=0 ; i<size ; i++) {
      for (int j=0 ; j<size ; j++) {
        squares[i][j] = new Square(i,j,(i+j)%2);
        if ((i+j)%2 != 0) {
          if ( (size / 2) < j) {
            piece = new Piece(i,j,PieceType.BLACK);
            blackPieces++;
            squares[i][j].setPiece(piece);
          } else if (3 +((size - 8) / 2) > j) {
            piece = new Piece(i,j,PieceType.WHITE);
            whitePieces++;
            squares[i][j].setPiece(piece);
          }
        }
      }
    }
    if(gameType.equals("Russian")) {
      System.out.println(gameType);
      rules = new RussianGameRules();
    } else if(gameType.equals("Classic")) {
      System.out.println(gameType);
      rules = new ClassicGameRules();
    }
    rules.setNumberOfPieces(blackPieces, whitePieces);
  }

  /**
   * Method for generating the response and sending it to the clients
   * Method is calling the methods from the GameRules interface object
   * @param in - stream for reading the data from the client
   * @param out_first - stream for sending the data to the first client
   * @param out_second - stream for sending the data to the second client
   * @param nextTurn - nextTurn of the game
   */
  public void generateResponse(BufferedReader in, PrintWriter out_first, PrintWriter out_second, int nextTurn) {
    try {
      line = in.readLine();
      System.out.println(line);
      String[] data_piece = line.split(" ");
      int oldX = parseInt(data_piece[0]);
      int oldY = parseInt(data_piece[1]);
      int newX = parseInt(data_piece[3]);
      int newY = parseInt(data_piece[4]);
      boolean classicCheck;
      String chosenPiece =  "( " + oldX + " " + oldY + " )";
      if (lock) {
        rules.availablePiece(turn, size, squares);
      }
      if (rules.getAvailablePiece().contains(chosenPiece)) {
        Piece tmpPiece = null;
        lock = true;
        if (rules.getObligatoryPiece().contains(chosenPiece)) {
          checkMoves = rules.checkMove(oldX, oldY, newX, newY);
          if (checkMoves) {
            if(rules.checkBlockedPieces(PieceType.BLACK)) {
              gameEnded = true;
              line = line + " WHITE WINS";
            } else if(rules.checkBlockedPieces(PieceType.WHITE)) {
              gameEnded = true;
              line = line + " BLACK WINS";
            }
            tmpPiece = squares[newX][newY].getPiece();
            checkMat = rules.checkIfMatted(oldX, oldY, newX, newY);
            if(gameType.equals("Classic")) {
              classicCheck = rules.availableMoves(tmpPiece).contains("kill");
            } else {
              classicCheck = false;
            }
            if (turn == 2 && newY == 0 && !classicCheck) {
              rules.promotion(newX, newY);
              line = line + " promotion";
            }
            if(turn == 1 && newY == size - 1 && !classicCheck) {
              line = line + " promotion";
              rules.promotion(newX, newY);
            }
          }
          if (checkMoves && checkMat) {
            int nrBlackPieces = rules.getBlackPieces();
            int nrWhitePieces = rules.getWhitePieces();
            if(nrBlackPieces == 0 && nrWhitePieces > 0) {
              gameEnded = true;
              line = line + " WHITE WINS";
            } else if(nrWhitePieces == 0 && nrBlackPieces > 0) {
              gameEnded = true;
              line = line + " BLACK WINS";
            }
            if (rules.availableMoves(tmpPiece).contains("kill")) { // check for double matting
              out_first.println("valid " + line + " matted turn");
              out_second.println("valid " + line + " matted turn");
              lock = false;
              String msg_ = " ( " + newX + " " + newY + " )";
              rules.setAvailablePiece(msg_);
              rules.setObligatoryPiece(msg_);
            } else { // single matting
              out_first.println("valid " + line + " matted");
              out_second.println("valid " + line + " matted");
              turn = nextTurn;
            }
          } else if (checkMoves) {
            out_first.println("valid " + line);
            out_second.println("valid " + line);
            turn = nextTurn;
          } else {
            out_first.println("not " + line);
            out_second.println("not " + line);
          }
        } else {
            out_first.println("not " + line);
            out_second.println("not " + line);
        }
      } else {
        out_first.println("not " + line);
        out_second.println("not " + line);
      }
    }
    catch(IOException err) {
      err.printStackTrace();
    }
  }

  /**
   * Thread method for initializing streams for each client, sending data to clients of their number, getting the data from the board and sending response to client
   */
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

        String msgFromBoard = in_first.readLine();
        String msgFromBoard_ = in_second.readLine();
        if(!msgFromBoard.equals(msgFromBoard_)) {
          throw new Exception("Not equal game types");
        }
        String[] info = msgFromBoard_.split(" ");
        size = parseInt(info[0]);
        gameType = info[1];
        createBoard();

        do {
          if (turn == SECOND) { // Ruch czarnych
            System.out.println("Turn Player 2");
            int nextTurn = FIRST;
            generateResponse(in_second, out_first, out_second, nextTurn);
          }
          if (turn == FIRST) { // Ruch białych
            System.out.println("Turn Player 1");
            int nextTurn = SECOND;
            generateResponse(in_first, out_first, out_second, nextTurn);
          }
        } while (!gameEnded);

      } catch (IOException ex) {
        System.err.println("ex");
      } catch (Exception e) {
        e.printStackTrace();
      }
  }
}
