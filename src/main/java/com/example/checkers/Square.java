package com.example.checkers;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Square class crating the rectangles
 * @author Bartłomiej Puchała Patryk Piskorski
 */
public class Square extends Rectangle {
    private Piece piece = null;

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    /**
     * @param x - x position of the square
     * @param y - y position of the square
     * @param type - type of the field
     */
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
