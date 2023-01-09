package com.example.checkers;

import java.util.ArrayList;

/**
 * Class including methods specifying rules of the russian checkers
 * @author Bartłomiej Puchała Patryk Piskorski
 */
public class RussianGameRules implements GameRules {

  private String availablePiece, obligatoryPiece;
  private int size = 0;
  public Square[][] squares = null;
  private int blackPieces, whitePieces = 0;

  private void setSquares(Square[][] squares) {
    this.squares = squares;
  }

  private void setSize(int size) {
    this.size = size;
  }

  public void setAvailablePiece(String availablePiece) {
    this.availablePiece = availablePiece;
  }

  public void setObligatoryPiece(String obligatoryPiece) {
    this.obligatoryPiece = obligatoryPiece;
  }

  public String getAvailablePiece() {
    return availablePiece;
  }

  public String getObligatoryPiece() {
    return obligatoryPiece;
  }

  public int getBlackPieces() {
    return blackPieces;
  }

  public int getWhitePieces() {
    return whitePieces;
  }

  public void setNumberOfPieces(int blackPieces, int whitePieces) {
    this.blackPieces = blackPieces;
    this.whitePieces = whitePieces;
  }

  /**
   * Method for checking moves of the piece and a king
   * @param piece - checked piece
   * @return specific String message depending on the move
   */
  @Override
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

  /**
   * Method is calling the method to check possible moves of the piece and is setting specific values based on return
   * @param turn - current turn in the game
   * @param size - size of the board
   * @param squares - squares object
   */
  @Override
  public void availablePiece(int turn, int size, Square[][] squares) {
    setSquares(squares);
    setSize(size);
    Piece newPiece;
    PieceType type;
    if (turn == 1) {
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

  /**
   * Method for checking if piece matted the other one
   * Method for reducing the white and blackPieces if they were matted
   * @param oldX - old x coordinate of piece
   * @param oldY - old y coordinate of piece
   * @param newX - new x coordinate of moved piece
   * @param newY - new y coordinate of moved piece
   * @return true if there was a mat, false otherwise
   */
  @Override
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

  /**
   * Method for checking if the piece move was valid(there is no other piece on board) and updating the squares
   * @param oldX - old x coordinate of piece
   * @param oldY - old y coordinate of piece
   * @param newX - new x coordinate of moved piece
   * @param newY - new y coordinate of moved piece
   * @return true if the move was valid, false otherwise
   */
  @Override
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

  /**
   * Method for changing the piece to king
   * @param x - x coordinate of the piece
   * @param y - y coordinate of the piece
   */
  @Override
  public void promotion(int x, int y) {
    squares[x][y].getPiece().setKing();
  }

  /**
   * @param type - type of piece
   * @return true if all the pieces of type are blocked(no possible move), false otherwise
   */
  @Override
  public boolean checkBlockedPieces(PieceType type) {
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

}
