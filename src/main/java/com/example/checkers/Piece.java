package com.example.checkers;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 * Piece class which creates the piece
 * @author Bartłomiej Puchała Patryk Piskorski
 */
public class Piece extends Circle {

    private int x_pos, y_pos; // position on board
    private double oldX, oldY; // old coordinates
    private double radius;
    private PieceType type;
    private boolean king = false;

    public boolean isKing() {
        return king;
    }

    /**
     * Method for setting the piece to king(changing gui)
     */
    public void setKing() {
        this.king = true;
        if(type == PieceType.WHITE) {
            setRadius(CheckersApp.PieceSize * 0.3);
            setFill(Color.rgb(1,1,1));
            setStroke(Color.rgb(255,255,255));
            setStrokeWidth(15);
        } else {
            setRadius(CheckersApp.PieceSize * 0.3);
            setFill(Color.rgb(255,255,255));
            setStroke(Color.rgb(1,1,1));
            setStrokeWidth(15);
        }
    }

    public PieceType getType() {
        return this.type;
    }
    public void setType(PieceType type) {
        this.type = type;
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
    }
    public double getOldY() {
        return oldY;
    }

    public void setOldY(double oldY) {
        this.oldY = oldY;
    }

    public boolean isHit(double x, double y) {
        return getBoundsInLocal().contains(x,y);
    }

    /**
     * Method creating the piece
     * @param x - x position of the piece
     * @param y - y position of the piece
     * @param type - type of the piece
     */
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
            setFill(Color.rgb(255,255,255));
        }
        else if(type.equals(PieceType.BLACK)){
            setFill(Color.rgb(1,1,1));
        }
    }

}
