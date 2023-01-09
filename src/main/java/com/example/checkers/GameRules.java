package com.example.checkers;

/**
 * Interface with the methods that server is checking
 * @author Patryk Piskorski Bartłomiej Puchała
 * @link{com.example.checkers.RussianGameRules}
 * @link{com.example.checkers.ClassicGameRules}
 */
public interface GameRules {
  String availableMoves(Piece piece);
  void availablePiece(int turn, int size, Square[][] squares);
  boolean checkIfMatted(int oldX, int oldY, int newX, int newY);
  boolean checkMove(int oldX, int oldY, int newX, int newY);
  void promotion(int x, int y);
  boolean checkBlockedPieces(PieceType type);
  void setNumberOfPieces(int blackPieces, int whitePieces);
  String getAvailablePiece();
  String getObligatoryPiece();
  int getBlackPieces();
  int getWhitePieces();
  void setAvailablePiece(String availablePiece);
  void setObligatoryPiece(String obligatoryPiece);

}
