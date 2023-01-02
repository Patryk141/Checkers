package com.example.checkers;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

public class Square extends Rectangle {
    private Piece piece = null;

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Square(int x, int y, int type) {
        setWidth(CheckersApp.PieceSize);
        setHeight(CheckersApp.PieceSize);
        relocate(x*CheckersApp.PieceSize,y*CheckersApp.PieceSize);
        if(type == 0){
            setFill(Color.rgb(227,197,140));
        }
        else{
            setFill(Color.rgb(149,95,34));
        }
    }
}
