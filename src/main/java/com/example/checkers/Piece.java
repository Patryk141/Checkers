package com.example.checkers;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.io.*;

public class Piece extends Circle implements Serializable {

    private int x_pos, y_pos;
    private double oldX, oldY, radius;
    PieceType type;
    private Circle circle;
    SerializableColor color;
    private transient Paint paint;

    public double getOldX() {
        return oldX;
    }
    public int getX_pos() {
        return this.x_pos;
    }
    public int getY_pos() {
        return this.y_pos;
    }
    public void setOldX(double oldX) {
        this.oldX = oldX;
        this.x_pos = (int) (oldX - CheckersApp.PieceSize*0.5)/CheckersApp.PieceSize;
    }
    public double getOldY() {
        return oldY;
    }

    public Paint getPaint() {
        return this.paint;
    }

    public PieceType getType() {
        return this.type;
    }

    public void setType(PieceType type) {
        this.type = type;
    }

    public void setOldY(double oldY) {
        this.oldY = oldY;
        this.y_pos = (int) (oldY - CheckersApp.PieceSize*0.5)/CheckersApp.PieceSize;
    }


    public boolean isHit(double x, double y) {
        return getBoundsInLocal().contains(x,y);
    }

    public Circle createCircle() {
        this.circle = new Circle(this.x_pos*CheckersApp.PieceSize+CheckersApp.PieceSize*0.5, this.y_pos*CheckersApp.PieceSize+CheckersApp.PieceSize*0.5,CheckersApp.PieceSize*0.4, this.getPaint());
        return this.circle;
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
            color = new SerializableColor(1, 1, 1);
            setFill(color.getFXColor());
        }
        else if(type.equals(PieceType.BLACK)){
            this.paint = Color.BLACK;
            color = new SerializableColor(0, 0, 0);
            setFill(color.getFXColor());
        }
//        setOnMouseDragged(new PieceEventHandler(player, x, y));
//        setOnMouseReleased(new PieceEventHandler(player, x, y));
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        setCenterX(in.readDouble());
        setCenterY(in.readDouble());
        setRadius(in.readDouble());
        setFill((Color)in.readObject());
//        setType((PieceType) in.readObject());
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
//        out.defaultWriteObject();

        out.writeDouble(this.circle.getCenterX());
        out.writeDouble(this.circle.getCenterY());
        out.writeDouble(this.circle.getRadius());
//        out.writeObject(color.getFXColor());
    }


}
