package com.example.checkers;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Piece extends Circle {
    public boolean isHit(double x, double y){
        return getBoundsInLocal().contains(x,y);
    }

    PieceType type;
    public Piece(int x, int y, PieceType type) {
        super(x*CheckersApp.PieceSize+CheckersApp.PieceSize*0.5,y*CheckersApp.PieceSize+CheckersApp.PieceSize*0.5,CheckersApp.PieceSize*0.4);
        if(type.equals(PieceType.WHITE)){
            setFill(Color.rgb(255,255,255));
        }
        else if(type.equals(PieceType.BLACK)){
            setFill(Color.rgb(1,1,1));
        }
        setOnMouseDragged(new PieceEventHandler());
        setOnMouseReleased(new PieceEventHandler());
    }
}
