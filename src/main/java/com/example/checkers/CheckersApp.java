package com.example.checkers;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CheckersApp extends Application {
    public static final int PieceSize = 80;
    private final int Size = 8;
    private final Square[][] squares = new Square[Size][Size];
    private static Player playerObj;

    private Pane CreateBoard(){
        Pane root = new Pane();
        Group squareGroup = new Group();
        Group pieceGroup = new Group();
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

    public void initApp() {
        Stage stage = new Stage();
        Task someTask = new Task<Player>() { // asynchroniczne zadania
            @Override
            protected Player call() throws Exception {
                return new Player();
            }
        };
        new Thread(someTask).start();
        Scene scene = new Scene(CreateBoard());
        stage.setTitle("Checkers");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void start(Stage stage) {
        initApp();
        initApp();
    }

    public static void main(String[] args) {
        launch(args);
    }
}