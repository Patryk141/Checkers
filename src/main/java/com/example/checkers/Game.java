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
  boolean checkMoves = false;
  private boolean checkMat = false;
  private boolean lock = true;
  private String line, availablePiece, obligatoryPiece;
  private Square[][] squares = new Square[Size][Size];

  public Game(Socket firstPlayer, Socket secondPlayer) {
    this.firstPlayer = firstPlayer;
    this.secondPlayer = secondPlayer;
  }

  public boolean isWinner() {
    return false;
  }

  public void createBoard() {
    for (i=0 ; i<Size ; i++) {
      for (j=0 ; j<Size ; j++) {
        squares[i][j] = new Square(i,j,(i+j)%2);
        if ((i+j)%2 != 0) {
          if (Size-4 < j) {
            piece = new Piece(i,j,PieceType.BLACK);
            squares[i][j].setPiece(piece);
          } else if (3 > j) {
            piece = new Piece(i,j,PieceType.WHITE);
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
    if (!piece.isKing()) {
      if (pos_X > 0) {
        tmp_X = pos_X - 1;
        if (type == PieceType.WHITE && pos_Y != Size - 1) {
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
      if (pos_X < Size - 1) {
        tmp_X = pos_X + 1;
        if (type == PieceType.WHITE && pos_Y != Size - 1) {
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
        if (squares[pos_X - 1][pos_Y - 1].getPiece() != null) {
          if (squares[pos_X - 2][pos_Y - 2].getPiece() == null && !squares[pos_X - 1][pos_Y - 1].getPiece().getType().equals(type)) {
            tmp_X = pos_X - 2;
            tmp_Y = pos_Y - 2;
            movesKill = movesKill + " kill ( " + tmp_X + " " + tmp_Y + " )";
          }
        }
      }
      if (pos_X - 2 >= 0 && pos_Y + 2 < Size) {
        if (squares[pos_X - 1][pos_Y + 1].getPiece() != null) {
          if (squares[pos_X - 2][pos_Y + 2].getPiece() == null && !squares[pos_X - 1][pos_Y + 1].getPiece().getType().equals(type)) {
            tmp_X = pos_X - 2;
            tmp_Y = pos_Y + 2;
            movesKill = movesKill + " kill ( " + tmp_X + " " + tmp_Y + " )";
          }
        }
      }
      if (pos_X + 2 < Size && pos_Y - 2 >= 0) {
        if (squares[pos_X + 1][pos_Y - 1].getPiece() != null) {
          if (squares[pos_X + 2][pos_Y - 2].getPiece() == null && !squares[pos_X + 1][pos_Y - 1].getPiece().getType().equals(type)) {
            tmp_X = pos_X + 2;
            tmp_Y = pos_Y - 2;
            movesKill = movesKill + " kill ( " + tmp_X + " " + tmp_Y + " )";
          }
        }
      }
      if (pos_X + 2 < Size && pos_Y + 2 < Size) {
        if (squares[pos_X + 1][pos_Y + 1].getPiece() != null) {
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
          while (tmp_X >= 0 && tmp_Y >= 0) {
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
      while (tmp_X < Size && tmp_Y >= 0) {
        if (squares[tmp_X][tmp_Y].getPiece() == null) {
          moves = moves + " ( " + tmp_X + " " + tmp_Y + " )";
        } else if (squares[tmp_X][tmp_Y].getPiece().getType() == type) {
          tmp_X = Size;
        } else {
          tmp_X++;
          tmp_Y--;
          while (tmp_X < Size && tmp_Y >= 0) {
            if (squares[tmp_X][tmp_Y].getPiece() == null) {
              movesKill = movesKill + " kill ( " + tmp_X + " " + tmp_Y + " )";
            } else {
              tmp_X = Size;
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
      while (tmp_X >= 0 && tmp_Y < Size) {
        if (squares[tmp_X][tmp_Y].getPiece() == null) {
          moves = moves + " ( " + tmp_X + " " + tmp_Y + " )";
        } else if (squares[tmp_X][tmp_Y].getPiece().getType() == type) {
          tmp_X = 0;
        } else {
          tmp_X--;
          tmp_Y++;
          while (tmp_X >= 0 && tmp_Y < Size) {
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
      while (tmp_X < Size && tmp_Y < Size) {
        if (squares[tmp_X][tmp_Y].getPiece() == null) {
          moves = moves + " ( " + tmp_X + " " + tmp_Y + " )";
        } else if (squares[tmp_X][tmp_Y].getPiece().getType() == type) {
          tmp_X = Size;
        } else {
          tmp_X++;
          tmp_Y++;
          while (tmp_X < Size && tmp_Y < Size) {
            if (squares[tmp_X][tmp_Y].getPiece() == null) {
              movesKill = movesKill + " kill ( " + tmp_X + " " + tmp_Y + " )";
            } else {
              tmp_X = Size;
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
    for (int i=0 ; i< Size ; i++) {
      for (int j=0 ; j< Size ; j++) {
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
//    if (((oldX + newX) % 2 == 0) && ((oldY + newY) % 2 == 0)) {
//      if (squares[(oldX + newX) / 2][(oldY + newY) / 2].getPiece().getType() != squares[newX][newY].getPiece().getType()) {
//        squares[(oldX + newX) / 2][(oldY + newY) / 2].setPiece(null);
//        return true;
//      }
//    }
//    return false;
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
  public void promotion(int x, int y) {
    squares[x][y].getPiece().setKing();
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
            System.out.println("Turn Player 2");
            line = in_second.readLine();
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
              lock = true;
              if (obligatoryPiece.contains(chosenPiece)) {
                checkMoves = checkMove(oldX, oldY, newX, newY);
                if (checkMoves) {
                  checkMat = checkIfMatted(oldX, oldY, newX, newY);
                  if (newY == 0) {
                    promotion(newX, newY);
                    line = line + " promotion";
                  }
                }
                if (checkMoves && checkMat) {
                  //Piece tmpPiece = new Piece(newX, newY, PieceType.BLACK);
                  Piece tmpPiece = squares[newX][newY].getPiece();
                  if (availableMoves(tmpPiece).contains("kill")) {
                    out_first.println("valid " + line + " matted turn");
                    out_second.println("valid " + line + " matted turn");
                    lock = false;
                    availablePiece = " ( " + newX + " " + newY + " )";
                    obligatoryPiece = availablePiece;
                  } else {
                    out_first.println("valid " + line + " matted");
                    out_second.println("valid " + line + " matted");
                    turn = FIRST;
                  }
                } else if (checkMoves) {
                  out_first.println("valid " + line);
                  out_second.println("valid " + line);
                  turn = FIRST;
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
                    squares[x][y].getPiece().setVisible(false);
                    squares[x][y].setPiece(null);
                  }
                  out_first.println("valid " + line + " remove" + obligatoryPiece);
                  out_second.println("valid " + line + " remove" + obligatoryPiece);
                  turn = FIRST;
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
          if (turn == FIRST) { // Ruch białych
            System.out.println("Turn Player 1");
            line = in_first.readLine();
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
              lock = true;
              if (obligatoryPiece.contains(chosenPiece)) {
                checkMoves = checkMove(oldX, oldY, newX, newY);
                if (checkMoves) {
                  checkMat = checkIfMatted(oldX, oldY, newX, newY);
                  if (newY == Size - 1) {
                    line = line + " promotion";
                    promotion(newX, newY);
                  }
                }
                if (checkMoves && checkMat) {
                  //Piece tmpPiece = new Piece(newX, newY, PieceType.WHITE);
                  Piece tmpPiece = squares[newX][newY].getPiece();
                  if (availableMoves(tmpPiece).contains("kill")) {
                    out_first.println("valid " + line + " matted turn");
                    out_second.println("valid " + line + " matted turn");
                    lock = false;
                    availablePiece = " ( " + newX + " " + newY + " )";
                    obligatoryPiece = availablePiece;
                  } else {
                    out_first.println("valid " + line + " matted");
                    out_second.println("valid " + line + " matted");
                    turn = SECOND;
                  }
                } else if (checkMoves) {
                  out_first.println("valid " + line);
                  out_second.println("valid " + line);
                  turn = SECOND;
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
                    squares[x][y].getPiece().setVisible(false);
                    squares[x][y].setPiece(null);
                  }
                  out_first.println("valid " + line + " remove" + obligatoryPiece);
                  out_second.println("valid " + line + " remove" + obligatoryPiece);
                  turn = SECOND;
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
        } while (!gameEnded);

      } catch (IOException ex) {
        System.err.println("ex");
      }
  }
}
