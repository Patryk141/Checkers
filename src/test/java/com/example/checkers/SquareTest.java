package com.example.checkers;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SquareTest {

  @Test
  void testSquare() {
    Square square = new Square(1, 1, 0);
    assertNotNull(square.getWidth());
    assertNotNull(square.getHeight());
    assertEquals(square.getFill(), Color.rgb(227,197,140));
  }
}