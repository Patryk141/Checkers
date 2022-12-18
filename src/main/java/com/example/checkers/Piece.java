package com.example.checkers;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Piece extends Circle {
    private double oldX, oldY;

    public double getOldX() {
        return oldX;
    }

    public void setOldX(double oldX) {
        this.oldX = oldX;
    }

    public double getOldY() {
        return oldY;
    }

    public void setOldY(double oldY) {
        this.oldY = oldY;
    }

    PieceType type;
    public boolean isHit(double x, double y){
        return getBoundsInLocal().contains(x,y);
    }
    public Piece(int x, int y, PieceType type) {
        super(x*CheckersApp.PieceSize+CheckersApp.PieceSize*0.5,y*CheckersApp.PieceSize+CheckersApp.PieceSize*0.5,CheckersApp.PieceSize*0.4);
        this.oldX = x*CheckersApp.PieceSize+CheckersApp.PieceSize*0.5;
        this.oldY = y*CheckersApp.PieceSize+CheckersApp.PieceSize*0.5;
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
