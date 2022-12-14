package com.example.checkers;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Square extends Rectangle {
    public Square(int x, int y, int type) {
        setWidth(CheckersApp.PieceSize);
        setHeight(CheckersApp.PieceSize);
        relocate(x*CheckersApp.PieceSize,y*CheckersApp.PieceSize);
        if(type == 0){
            setFill(Color.rgb(255,255,255));
        }
        else{
            setFill(Color.rgb(80,100,60));
        }
    }
}
