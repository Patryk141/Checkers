package com.example.checkers;

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
  void setBlackPieces(int blackPieces);
  void setWhitePieces(int whitePieces);

}
