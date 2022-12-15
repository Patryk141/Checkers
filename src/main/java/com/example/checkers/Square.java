package com.example.checkers;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Square extends Rectangle {

    public Square(int x, int y, int type) {
        super(x*CheckersApp.PieceSize,y*CheckersApp.PieceSize, CheckersApp.PieceSize, CheckersApp.PieceSize);
        if(type == 0){
            setFill(Color.rgb(255,255,255));
        }
        else if(type == 1){
            setFill(Color.rgb(80,100,60));
        }
    }
}
