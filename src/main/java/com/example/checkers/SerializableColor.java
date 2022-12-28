package com.example.checkers;

import javafx.scene.paint.Color;

import java.io.Serializable;

public class SerializableColor implements Serializable {
  private int red;
  private int green;
  private int blue;

//  public SerializableColor(Color color) {
//    this.red = color.getRed();
//    this.green = color.getGreen();
//    this.blue = color.getBlue();
//  }

  public SerializableColor(int red, int green, int blue) {
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  public Color getFXColor() {
    return new Color(red, green, blue, 1);
  }

}
