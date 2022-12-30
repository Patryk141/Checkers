package com.example.checkers;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.io.*;

public class Piece extends Circle implements Serializable {

    private int x_pos, y_pos; // position on board
    private double oldX, oldY; // old coordinates
    private double radius;
    PieceType type;
    private Paint paint;

    public Paint getPaint() {
        return this.paint;
    }

    public void setX_pos(int x_pos) {
        this.x_pos = x_pos;
    }

    public void setY_pos(int y_pos) {
        this.y_pos = y_pos;
    }

    public int getX_pos() {
        return this.x_pos;
    }
    public int getY_pos() {
        return this.y_pos;
    }

    public double getOldX() {
        return oldX;
    }

    public void setOldX(double oldX) {
        this.oldX = oldX;
//        this.x_pos = (int) (oldX - CheckersApp.PieceSize*0.5)/CheckersApp.PieceSize;
    }
    public double getOldY() {
        return oldY;
    }

    public void setOldY(double oldY) {
        this.oldY = oldY;
//        this.y_pos = (int) (oldY - CheckersApp.PieceSize*0.5)/CheckersApp.PieceSize;
    }


    public boolean isHit(double x, double y) {
        return getBoundsInLocal().contains(x,y);
    }


    public Piece(int x, int y, PieceType type) {
        super(x*CheckersApp.PieceSize+CheckersApp.PieceSize*0.5, y*CheckersApp.PieceSize+CheckersApp.PieceSize*0.5, CheckersApp.PieceSize*0.4);
        setCenterX(x*CheckersApp.PieceSize+CheckersApp.PieceSize*0.5);
        setCenterY(y*CheckersApp.PieceSize+CheckersApp.PieceSize*0.5);
        this.x_pos = x;
        this.y_pos = y;
        this.type = type;
        this.radius = CheckersApp.PieceSize*0.4;

        this.oldX = x*CheckersApp.PieceSize+CheckersApp.PieceSize*0.5;
        this.oldY = y*CheckersApp.PieceSize+CheckersApp.PieceSize*0.5;

        if(type.equals(PieceType.WHITE)){
            this.paint = Color.WHITE;
            setFill(Color.rgb(255,255,255));
        }
        else if(type.equals(PieceType.BLACK)){
            this.paint = Color.BLACK;
            setFill(Color.rgb(1,1,1));
        }
//        setOnMouseDragged(new PieceEventHandler(player, x, y));
//        setOnMouseReleased(new PieceEventHandler(player, x, y));
    }


}
