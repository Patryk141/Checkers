package com.example.checkers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class PieceEventHandler implements EventHandler<MouseEvent> {
    Piece piece;
    private double x;
    private double y;

    private void move(MouseEvent e){
        double dx = e.getX() - x;
        double dy = e.getY() - y;
        if(piece.isHit(x,y)){
            piece.setCenterX(piece.getCenterX() + dx);
            piece.setCenterY(piece.getCenterY() + dy);
        }
        x += dx;
        y += dy;
    }
    private void moveCentre(MouseEvent e){
        piece.setCenterX(piece.getCenterX() - piece.getCenterX()%CheckersApp.PieceSize + CheckersApp.PieceSize*0.5);
        piece.setCenterY(piece.getCenterY() - piece.getCenterY()%CheckersApp.PieceSize + CheckersApp.PieceSize*0.5);
    }
    @Override
    public void handle(MouseEvent e){
        piece = (Piece)e.getSource();
        if(e.getEventType() == MouseEvent.MOUSE_DRAGGED){
            move(e);
        }
        if(e.getEventType() == MouseEvent.MOUSE_RELEASED){
            moveCentre(e);
        }
    }
}
