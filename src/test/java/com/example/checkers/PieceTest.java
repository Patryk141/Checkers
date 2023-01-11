package com.example.checkers;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PieceTest {

  @Test
  public void createPiece() {
    Piece piece = new Piece(0, 1, PieceType.WHITE);
    assertEquals(piece.getType(), PieceType.WHITE);
    assertEquals(piece.getX_pos(), 0);
    assertEquals(piece.getY_pos(), 1);
    assertFalse(piece.isKing());
    assertEquals(piece.getOldX(), piece.getX_pos()*CheckersApp.PieceSize+CheckersApp.PieceSize*0.5);
    assertEquals(piece.getOldY(), piece.getY_pos()*CheckersApp.PieceSize+CheckersApp.PieceSize*0.5);
    assertEquals(piece.getCenterX(), piece.getX_pos()*CheckersApp.PieceSize+CheckersApp.PieceSize*0.5);
  }

  @Test
  public void testKing() {
    Piece piece = new Piece(0, 5, PieceType.BLACK);
    piece.setKing();
    assertTrue(piece.isKing());
    assertEquals(piece.getRadius(), CheckersApp.PieceSize * 0.3);
    assertNotEquals(piece.getFill(), Color.rgb(1,1,1));
    assertEquals(piece.getStroke(),  Color.rgb(1,1,1));
    assertEquals(piece.getFill(), Color.rgb(255,255,255));
    assertTrue(piece.getStrokeWidth() == 15);
  }

}
