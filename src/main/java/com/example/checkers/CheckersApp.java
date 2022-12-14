package com.example.checkers;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CheckersApp extends Application {
    public static final int PieceSize = 100;
    private final int Size = 7;
    private final Square[][] squares = new Square[Size][Size];
    private final Group squareGroup = new Group();
    private final Group pieceGroup = new Group();
    private Pane CreateBoard(){
        Pane root = new Pane();
        root.setPrefSize(Size*PieceSize,Size*PieceSize);
        for(int i=0 ; i<Size ; i++){
            for(int j=0 ; j<Size ; j++){
                squares[i][j] = new Square(i,j,(i+j)%2);
                if((i+j)%2 != 0){
                    if(Size-4 < j){
                        Piece piece = new Piece(i,j,PieceType.BLACK);
                        pieceGroup.getChildren().add(piece);
                    }
                    else if(3 > j){
                        Piece piece = new Piece(i,j,PieceType.WHITE);
                        pieceGroup.getChildren().add(piece);
                    }
                }
                squareGroup.getChildren().add(squares[i][j]);
            }
        }
        root.getChildren().addAll(squareGroup,pieceGroup);
        return root;
    }
    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(CreateBoard());
        stage.setTitle("Checkers");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}