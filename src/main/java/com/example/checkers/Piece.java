package com.example.checkers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.PrintWriter;

public class Piece extends StackPane {

    private double X,Y;
    private PrintWriter out;
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
        circle.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("Handling click on Piece class");
                circle.setFill(Color.rgb(35, 100, 35));
            }
        });
//        setOnMousePressed(e -> {
//            X = x;
//            Y = y;
//            System.out.println(x + " " + y);
//        });
        getChildren().add(circle);
    }

    public void setOutput(PrintWriter out) {
        this.out = out;
    }


}
