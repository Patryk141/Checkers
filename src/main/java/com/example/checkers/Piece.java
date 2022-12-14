package com.example.checkers;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Piece extends StackPane {
    private double X,Y;
    PieceType type;
    public Piece(int x, int y, PieceType type) {
        this.type = type;
        Circle circle = new Circle();
        circle.setRadius(CheckersApp.PieceSize*0.4);
        relocate(x*CheckersApp.PieceSize+CheckersApp.PieceSize*0.1,y*CheckersApp.PieceSize+CheckersApp.PieceSize*0.1);
        if(type.equals(PieceType.WHITE)){
            circle.setFill(Color.rgb(255,255,255));
        }
        else if(type.equals(PieceType.BLACK)){
            circle.setFill(Color.rgb(1,1,1));
        }
        setOnMousePressed(e -> {
            X = e.getSceneX();
            Y = e.getSceneY();
            System.out.println(x + " " + y);
        });
        getChildren().add(circle);
    }
}
