package com.example.checkers;

import javafx.scene.input.MouseEvent;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class Game extends CheckersApp implements Runnable {

  private Socket firstPlayer;
  private Socket secondPlayer;
  private boolean gameEnded = false;
  public static int size;
  private final static int FIRST=1; // Białe
  private final static int SECOND=2; // Czarne
  private static int turn=FIRST;
  boolean checkMoves = false;
  private boolean checkMat = false;
  private boolean lock = true;
  private String line, availablePiece, obligatoryPiece;
  private Square[][] squares;
  int blackPieces, whitePieces = 0;

  public Game(Socket firstPlayer, Socket secondPlayer) {
    this.firstPlayer = firstPlayer;
    this.secondPlayer = secondPlayer;
  }

  public boolean isWinner() {
    return false;
  }

  public void createBoard() {

    squares = new Square[size][size];
    System.out.println(size);
    for (i=0 ; i<size ; i++) {
      for (j=0 ; j<size ; j++) {
        squares[i][j] = new Square(i,j,(i+j)%2);
        if ((i+j)%2 != 0) {
          if (size-4 < j) {
            piece = new Piece(i,j,PieceType.BLACK);
            blackPieces++;
            squares[i][j].setPiece(piece);
          } else if (3 > j) {
            piece = new Piece(i,j,PieceType.WHITE);
            whitePieces++;
            squares[i][j].setPiece(piece);
          }
        }
      }
    }
  }

  public String availableMoves(Piece piece) {
    String moves = "";
    String movesKill = "";
    int pos_X = piece.getX_pos();
    int pos_Y = piece.getY_pos();
    int tmp_X, tmp_Y;
    PieceType type = piece.getType();

    if (!piece.isKing()) { // piece is not a king
      if (pos_X > 0) { // check moves on one diagonal
        tmp_X = pos_X - 1;
        if (type == PieceType.WHITE && pos_Y != size - 1) {
          tmp_Y = pos_Y + 1;
          if (squares[tmp_X][tmp_Y].getPiece() == null) {
            moves = moves + " ( " + tmp_X + " " + tmp_Y + " )";
          }
        } else if (type == PieceType.BLACK && pos_Y != 0) {
          tmp_Y = pos_Y - 1;
          if (squares[tmp_X][tmp_Y].getPiece() == null) {
            moves = moves + " ( " + tmp_X + " " + tmp_Y + " )";
          }
        }
      }
      if (pos_X < size - 1) { // check moves on second diagonal
        tmp_X = pos_X + 1;
        if (type == PieceType.WHITE && pos_Y != size - 1) {
          tmp_Y = pos_Y + 1;
          if (squares[tmp_X][tmp_Y].getPiece() == null) {
            moves = moves + " ( " + tmp_X + " " + tmp_Y + " )";
          }
        } else if (type == PieceType.BLACK && pos_Y != 0) {
          tmp_Y = pos_Y - 1;
          if (squares[tmp_X][tmp_Y].getPiece() == null) {
            moves = moves + " ( " + tmp_X + " " + tmp_Y + " )";
          }
        }
      }
      if (pos_X - 2 >= 0 && pos_Y - 2 >= 0) {
        if (squares[pos_X - 1][pos_Y - 1].getPiece() != null) { // left up
          if (squares[pos_X - 2][pos_Y - 2].getPiece() == null && !squares[pos_X - 1][pos_Y - 1].getPiece().getType().equals(type)) {
            tmp_X = pos_X - 2;
            tmp_Y = pos_Y - 2;
            movesKill = movesKill + " kill ( " + tmp_X + " " + tmp_Y + " )";
          }
        }
      }
      if (pos_X - 2 >= 0 && pos_Y + 2 < size) {
        if (squares[pos_X - 1][pos_Y + 1].getPiece() != null) { // left bottom
          if (squares[pos_X - 2][pos_Y + 2].getPiece() == null && !squares[pos_X - 1][pos_Y + 1].getPiece().getType().equals(type)) {
            tmp_X = pos_X - 2;
            tmp_Y = pos_Y + 2;
            movesKill = movesKill + " kill ( " + tmp_X + " " + tmp_Y + " )";
          }
        }
      }
      if (pos_X + 2 < size && pos_Y - 2 >= 0) {
        if (squares[pos_X + 1][pos_Y - 1].getPiece() != null) { // right up
          if (squares[pos_X + 2][pos_Y - 2].getPiece() == null && !squares[pos_X + 1][pos_Y - 1].getPiece().getType().equals(type)) {
            tmp_X = pos_X + 2;
            tmp_Y = pos_Y - 2;
            movesKill = movesKill + " kill ( " + tmp_X + " " + tmp_Y + " )";
          }
        }
      }
      if (pos_X + 2 < size && pos_Y + 2 < size) {
        if (squares[pos_X + 1][pos_Y + 1].getPiece() != null) { // right bottom
          if (squares[pos_X + 2][pos_Y + 2].getPiece() == null && !squares[pos_X + 1][pos_Y + 1].getPiece().getType().equals(type)) {
            tmp_X = pos_X + 2;
            tmp_Y = pos_Y + 2;
            movesKill = movesKill + " kill ( " + tmp_X + " " + tmp_Y + " )";
          }
        }
      }
    } else {
      tmp_X = pos_X - 1;
      tmp_Y = pos_Y - 1;
      while (tmp_X >= 0 && tmp_Y >= 0) {
        if (squares[tmp_X][tmp_Y].getPiece() == null) {
          moves = moves + " ( " + tmp_X + " " + tmp_Y + " )";
        } else if (squares[tmp_X][tmp_Y].getPiece().getType() == type) {
          tmp_X = 0;
        } else {
          tmp_X--;
          tmp_Y--;
          while (tmp_X >= 0 && tmp_Y >= 0) { // every square on diagonal behind occupied piece
            if (squares[tmp_X][tmp_Y].getPiece() == null) {
              movesKill = movesKill + " kill ( " + tmp_X + " " + tmp_Y + " )";
            } else {
              tmp_X = 0;
            }
            tmp_X--;
            tmp_Y--;
          }
        }
        tmp_X--;
        tmp_Y--;
      }

      tmp_X = pos_X + 1;
      tmp_Y = pos_Y - 1;
      while (tmp_X < size && tmp_Y >= 0) {
        if (squares[tmp_X][tmp_Y].getPiece() == null) {
          moves = moves + " ( " + tmp_X + " " + tmp_Y + " )";
        } else if (squares[tmp_X][tmp_Y].getPiece().getType() == type) {
          tmp_X = size;
        } else {
          tmp_X++;
          tmp_Y--;
          while (tmp_X < size && tmp_Y >= 0) {
            if (squares[tmp_X][tmp_Y].getPiece() == null) {
              movesKill = movesKill + " kill ( " + tmp_X + " " + tmp_Y + " )";
            } else {
              tmp_X = size;
            }
            tmp_X++;
            tmp_Y--;
          }
        }
        tmp_X++;
        tmp_Y--;
      }

      tmp_X = pos_X - 1;
      tmp_Y = pos_Y + 1;
      while (tmp_X >= 0 && tmp_Y < size) {
        if (squares[tmp_X][tmp_Y].getPiece() == null) {
          moves = moves + " ( " + tmp_X + " " + tmp_Y + " )";
        } else if (squares[tmp_X][tmp_Y].getPiece().getType() == type) {
          tmp_X = 0;
        } else {
          tmp_X--;
          tmp_Y++;
          while (tmp_X >= 0 && tmp_Y < size) {
            if (squares[tmp_X][tmp_Y].getPiece() == null) {
              movesKill = movesKill + " kill ( " + tmp_X + " " + tmp_Y + " )";
            } else {
              tmp_X = 0;
            }
            tmp_X--;
            tmp_Y++;
          }
        }
        tmp_X--;
        tmp_Y++;
      }

      tmp_X = pos_X + 1;
      tmp_Y = pos_Y + 1;
      while (tmp_X < size && tmp_Y < size) {
        if (squares[tmp_X][tmp_Y].getPiece() == null) {
          moves = moves + " ( " + tmp_X + " " + tmp_Y + " )";
        } else if (squares[tmp_X][tmp_Y].getPiece().getType() == type) {
          tmp_X = size;
        } else {
          tmp_X++;
          tmp_Y++;
          while (tmp_X < size && tmp_Y < size) {
            if (squares[tmp_X][tmp_Y].getPiece() == null) {
              movesKill = movesKill + " kill ( " + tmp_X + " " + tmp_Y + " )";
            } else {
              tmp_X = size;
            }
            tmp_X++;
            tmp_Y++;
          }
        }
        tmp_X++;
        tmp_Y++;
      }
    }
    if (movesKill == "") {
      return moves;
    }
    return movesKill;
  }

  public void availablePiece() {
    Piece newPiece;
    PieceType type;
    if (turn == FIRST) {
      type = PieceType.WHITE;
    } else {
      type = PieceType.BLACK;
    }
    availablePiece = "";
    obligatoryPiece = "";
    for (int i=0 ; i< size ; i++) {
      for (int j=0 ; j< size ; j++) {
        newPiece = squares[i][j].getPiece();
        if (newPiece != null) {
          if (newPiece.getType() == type) {
            if (availableMoves(newPiece) != "") {
              availablePiece = availablePiece + " ( " + i + " " + j + " )";
            }
            if(availableMoves(newPiece).contains("kill")) {
              obligatoryPiece = obligatoryPiece + " ( " + i + " " + j + " )";
            }
          }
        }
      }
    }
    if (obligatoryPiece == "") {
      obligatoryPiece = availablePiece;
    }
  }

  public boolean checkIfMatted(int oldX, int oldY, int newX, int newY) {
    while (oldX != newX) {
      if (oldX < newX) {
        oldX = oldX + 1;
      } else {
        oldX = oldX - 1;
      }
      if (oldY < newY) {
        oldY = oldY + 1;
      } else {
        oldY = oldY - 1;
      }
      if (squares[oldX][oldY].getPiece() != null && oldX != newX) {
        if(squares[oldX][oldY].getPiece().getType() == PieceType.WHITE) {
          whitePieces--;
        }
        if(squares[oldX][oldY].getPiece().getType() == PieceType.BLACK) {
          blackPieces--;
        }
        squares[oldX][oldY].setPiece(null);
        return true;
      }
    }
    return false;
  }

  public boolean checkMove(int oldX, int oldY, int newX, int newY) {
    if ((newX + newY) % 2 == 1) {
      if (squares[newX][newY].getPiece() == null) {
        Piece newPiece = squares[oldX][oldY].getPiece();
        String my_move = availableMoves(newPiece);
        System.out.println(my_move);
        if (my_move.contains("( " + newX + " " + newY + " )")) {
          squares[oldX][oldY].setPiece(null);
          newPiece.setCenterX(newX*CheckersApp.PieceSize+CheckersApp.PieceSize*0.5);
          newPiece.setCenterY(newY*CheckersApp.PieceSize+CheckersApp.PieceSize*0.5);
          newPiece.setX_pos(newX);
          newPiece.setY_pos(newY);
          newPiece.setOldX(newPiece.getCenterX() - newPiece.getCenterX() % CheckersApp.PieceSize + CheckersApp.PieceSize * 0.5);
          newPiece.setOldY(newPiece.getCenterY() - newPiece.getCenterY() % CheckersApp.PieceSize + CheckersApp.PieceSize * 0.5);
          squares[newX][newY].setPiece(newPiece);
          return true;
        }
      }
    }
    return false;
  }
  // setting the king
  public void promotion(int x, int y) {
    squares[x][y].getPiece().setKing();
  }

  private boolean checkBlockedPieces(PieceType type) {
    ArrayList<Piece> pieceArr = new ArrayList<Piece>();
    int blockedPieces = 0;

    for(int i = 0; i < squares.length; i++) {
      for(int j = 0; j < squares.length; j++) {
        Piece tmpPiece = squares[i][j].getPiece();
        if(tmpPiece != null && tmpPiece.getType() == type) {
          pieceArr.add(tmpPiece);
        }
      }
    }
    for(Piece piece: pieceArr) {
      if(availableMoves(piece) == "") {
        blockedPieces++;
      }
    }
    if(blockedPieces == pieceArr.size()) { // all white or black pieces are blocked
     return true;
    }
    return false;
  }

  private String checkWinner() {
    System.out.println(blackPieces);
    System.out.println(whitePieces);

    if(blackPieces == 0 && whitePieces > 0) {
      gameEnded = true;
      return "WHITE WINS";
    }

    if(checkBlockedPieces(PieceType.WHITE)) {
      gameEnded = true;
      return "BLACK WINS BY BLOCKING ALL WHITES";
    }

    if(checkBlockedPieces(PieceType.BLACK)) {
      gameEnded = true;
      return "WHITE WINS BY BLOCKING ALL BlACKS";
    }

    if(whitePieces == 0 && blackPieces > 0) {
      gameEnded = true;
      return "BLACK WINS";
    }

    return "NO WINNER";
  }

  private void generateResponse(BufferedReader in, PrintWriter out_first, PrintWriter out_second, int nextTurn) {
    try {
      line = in.readLine();
      System.out.println(line);
      String[] data_piece = line.split(" ");
      int oldX = parseInt(data_piece[0]);
      int oldY = parseInt(data_piece[1]);
      int newX = parseInt(data_piece[3]);
      int newY = parseInt(data_piece[4]);

      String chosenPiece =  "( " + oldX + " " + oldY + " )";
      if (lock) {
        availablePiece();
      }
      if (availablePiece.contains(chosenPiece)) {
        Piece tmpPiece = null;
        lock = true;
        if (obligatoryPiece.contains(chosenPiece)) {
          checkMoves = checkMove(oldX, oldY, newX, newY);
          if (checkMoves) {
            if(checkBlockedPieces(PieceType.BLACK)) {
              gameEnded = true;
              line = line + " WHITE WINS";
            } else if(checkBlockedPieces(PieceType.WHITE)) {
              gameEnded = true;
              line = line + " BLACK WINS";
            }
            tmpPiece = squares[newX][newY].getPiece();
            checkMat = checkIfMatted(oldX, oldY, newX, newY);
            if (turn == 2 && newY == 0) {
              promotion(newX, newY);
              line = line + " promotion";
            }
            if(turn == 1 && newY == size - 1) {
              line = line + " promotion";
              promotion(newX, newY);
            }
          }
          if (checkMoves && checkMat) {
            if(blackPieces == 0 && whitePieces > 0) {
              gameEnded = true;
              line = line + " WHITE WINS";
            } else if(whitePieces == 0 && blackPieces > 0) {
              gameEnded = true;
              line = line + " BLACK WINS";
            }
            //Piece tmpPiece = new Piece(newX, newY, PieceType.BLACK);
//            Piece tmpPiece = squares[newX][newY].getPiece();
            if (availableMoves(tmpPiece).contains("kill")) { // check for double matting
              out_first.println("valid " + line + " matted turn");
              out_second.println("valid " + line + " matted turn");
              lock = false;
              availablePiece = " ( " + newX + " " + newY + " )";
              obligatoryPiece = availablePiece;
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
          checkMoves = checkMove(oldX, oldY, newX, newY);
          if (checkMoves) {
            String[] date_xy = obligatoryPiece.split(" \\( ");
            int x, y;
            for (int i=1 ; i<date_xy.length ; i++) {
              String[] piecexy = date_xy[i].split(" ");
              x = parseInt(piecexy[0]);
              y = parseInt(piecexy[1]);
              if(squares[x][y].getPiece().getType() == PieceType.WHITE) {
                whitePieces--;
              }
              if(squares[x][y].getPiece().getType() == PieceType.BLACK) {
                blackPieces--;
              }
              if((blackPieces == 0 && whitePieces > 0) || checkBlockedPieces(PieceType.BLACK)) {
                gameEnded = true;
                line = line + " WHITE WINS";
              } else if((whitePieces == 0 && blackPieces > 0) || checkBlockedPieces(PieceType.WHITE)) {
                gameEnded = true;
                line = line + " BLACK WINS";
              }
              squares[x][y].getPiece().setVisible(false);
              squares[x][y].setPiece(null);
            }
            out_first.println("valid " + line + " remove" + obligatoryPiece);
            out_second.println("valid " + line + " remove" + obligatoryPiece);
            turn = nextTurn;
          } else {
            out_first.println("not " + line);
            out_second.println("not " + line);
          }
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

        String msg2 = in_first.readLine();
        msg2 = in_second.readLine();
        size = parseInt(msg2);
        createBoard();
        do {
          if (turn == SECOND) { // Ruch czarnych
            System.out.println("Turn Player 2");
            int nextTurn = FIRST;
            generateResponse(in_second, out_first, out_second, nextTurn);
            String msg = checkWinner();
            System.out.println(msg);
          }
          if (turn == FIRST) { // Ruch białych
            System.out.println("Turn Player 1");
            int nextTurn = SECOND;
            generateResponse(in_first, out_first, out_second, nextTurn);
            String msg = checkWinner();
            System.out.println(msg);
          }
        } while (!gameEnded);

      } catch (IOException ex) {
        System.err.println("ex");
      }
  }
}
