package com.example.checkers;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

import static java.lang.Integer.parseInt;

public class CheckersApp extends Application implements Runnable, EventHandler<MouseEvent> {

    public static final int PieceSize = 80;
    //    private Player player;
    public static final int Size = 8;
    int i, j;
    Piece piece;
    public static final Square[][] squares = new Square[Size][Size];

    Socket socket = null;
    PrintWriter out = null;
    BufferedReader in = null;
    private int player;
    public final static int PLAYER1 = 1;
    public final static int PLAYER2 = 2;
    public static int ACTIVE = 0;
    public static final int NONACTIVE = 1;
    private static int currentPlayer = PLAYER1; // ustawienie obecnego gracza na gracza, który zaczyna białymi

    private static int showing = ACTIVE;
    private boolean yourTurn = false;
    private String move = "";
    private int new_coordinate_x, new_coordinate_y, old_coordinate_x, old_coordinate_y;
    private Pane CreateBoard() {

        Pane root = new Pane();
        Group squareGroup = new Group();
        Group pieceGroup = new Group();
        root.setPrefSize(Size*PieceSize,Size*PieceSize);
        for(i=0 ; i<Size ; i++) {
            for(j=0 ; j<Size ; j++){
                squares[i][j] = new Square(i,j,(i+j)%2);
                if((i+j)%2 != 0) {
                    if(Size-4 < j){
                        piece = new Piece(i,j,PieceType.BLACK);
                        squares[i][j].setPiece(piece); // ustawienie na kwadratach pionków
                        if(player == PLAYER2) {
                            piece.setOnMouseDragged(this);
                            piece.setOnMouseReleased(this);
                        }
                        pieceGroup.getChildren().add(piece);
                    }
                    else if(3 > j){
                        piece = new Piece(i,j,PieceType.WHITE);
                        squares[i][j].setPiece(piece);
                        if(player == PLAYER1) {
                            piece.setOnMouseDragged(this);
                            piece.setOnMouseReleased(this);
                        }
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
    public void handle(MouseEvent e) {

        Piece currPiece = (Piece)e.getSource();
        if (e.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            double dx = e.getX() - currPiece.getCenterX();
            double dy = e.getY() - currPiece.getCenterY();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (currPiece.isHit(currPiece.getCenterX(), currPiece.getCenterY())) {
                        currPiece.setCenterX(currPiece.getCenterX() + dx);
                        currPiece.setCenterY(currPiece.getCenterY() + dy);
                    }
                }
            });
        }
        if (e.getEventType() == MouseEvent.MOUSE_RELEASED) {
            try {
                PrintWriter dataPiece = new PrintWriter(socket.getOutputStream(), true);
                new_coordinate_x = (int) (e.getX() - e.getX()%PieceSize)/PieceSize;
                new_coordinate_y = (int) (e.getY() - e.getY()%PieceSize)/PieceSize;
                if (yourTurn == true) {
                    System.out.println("Sending data to server...");
                    System.out.println(currPiece.getX_pos() + " " + currPiece.getY_pos() + " -> " + new_coordinate_x + " " + new_coordinate_y);
                    dataPiece.println(currPiece.getX_pos() + " " + currPiece.getY_pos() + " -> " + new_coordinate_x + " " + new_coordinate_y);
                }
                else {
                    currPiece.setCenterX(currPiece.getOldX());
                    currPiece.setCenterY(currPiece.getOldY());
                }
            } catch(IOException ex) {
                System.out.println("blad");
                ex.printStackTrace();
            }
            showing = ACTIVE;
            currentPlayer = player;
        }
    }


    private void sendMove(MouseEvent e, Piece piece, int x_pos, int y_pos) {

    }

    private void receiveMove() { // both player 1 and player 2 call this method after one of them makes move
        try {
            // Odebranie stanu gry i zmienienie gui
            move = in.readLine();
            System.out.println(move);
            if (move.contains("remove")) {
                remove(move);
            }
            if (move.contains("valid")) {
                if (move.contains("promotion")) {
                    promotionPiece(move);
                }
                changePosition(move);
                if (move.contains("matted")) {
                    removePiece(move);
                    if (move.contains("turn")) {
                        if (yourTurn == true) {
                            yourTurn = false;
                        }
                        else {
                            yourTurn = true;
                        }
                    }
//                    old_coordinate_x = new_coordinate_x;
//                    old_coordinate_y = new_coordinate_y;
                }
                if (yourTurn == true) {
                    yourTurn = false;
                }
                else {
                    yourTurn = true;
                }
            }
            else if (move.contains("not")){
                backPosition(move);
            }
        } catch(IOException e) {
            System.out.println("Read failed");
            System.exit(1);
        }
    }
    private void promotionPiece(String move) {
        String[] date_xy = move.split(" ");
        squares[parseInt(date_xy[1])][parseInt(date_xy[2])].getPiece().setKing();
    }
    private void backPosition(String move){
        String[] date_xy = move.split(" ");
        Piece mypiece = squares[parseInt(date_xy[1])][parseInt(date_xy[2])].getPiece();
        mypiece.setCenterX(mypiece.getOldX());
        mypiece.setCenterY(mypiece.getOldY());
    }
    private void changePosition(String move) {
        String[] date_xy = move.split(" ");
        Piece mypiece = squares[parseInt(date_xy[1])][parseInt(date_xy[2])].getPiece();
        mypiece.setCenterX(parseInt(date_xy[4])*CheckersApp.PieceSize+CheckersApp.PieceSize*0.5);
        mypiece.setCenterY(parseInt(date_xy[5])*CheckersApp.PieceSize+CheckersApp.PieceSize*0.5);
        mypiece.setX_pos(parseInt(date_xy[4])); // setting new x_pos
        mypiece.setY_pos(parseInt(date_xy[5])); // setting new y_pos
        mypiece.setOldX(mypiece.getCenterX() - mypiece.getCenterX() % CheckersApp.PieceSize + CheckersApp.PieceSize * 0.5);
        mypiece.setOldY(mypiece.getCenterY() - mypiece.getCenterY() % CheckersApp.PieceSize + CheckersApp.PieceSize * 0.5);
        squares[parseInt(date_xy[1])][parseInt(date_xy[2])].setPiece(null); // setting old piece to null
        squares[mypiece.getX_pos()][mypiece.getY_pos()].setPiece(mypiece); // setting new piece
    }
    private void removePiece(String move) {
        String[] date_xy = move.split(" ");
//        Piece removedPiece = squares[(parseInt(date_xy[1]) + parseInt(date_xy[4])) / 2][(parseInt(date_xy[2]) + parseInt(date_xy[5])) / 2].getPiece();
//        squares[(parseInt(date_xy[1]) + parseInt(date_xy[4])) / 2][(parseInt(date_xy[2]) + parseInt(date_xy[5])) / 2].setPiece(null);
//        removedPiece.setVisible(false);
        int oldX = parseInt(date_xy[1]);
        int oldY = parseInt(date_xy[2]);
        int newX = parseInt(date_xy[4]);
        int newY = parseInt(date_xy[5]);
        while (oldX != newX) {
            if (oldX < newX) {
                oldX = oldX + 1;
            } else {
                oldX = oldX - 1;
            }
            if (oldY < newY) {
                oldY = oldY + 1;
            } else {
                oldY = oldY - 1;
            }
            if (squares[oldX][oldY].getPiece() != null && oldX != newX) {
                squares[oldX][oldY].getPiece().setVisible(false);
                squares[oldX][oldY].setPiece(null);
            }
        }
    }
    private void remove(String move) {
        String[] date_xy = move.split(" \\( ");
        int x, y;
        for (int i=1 ; i<date_xy.length ; i++) {
            String[] piecexy = date_xy[i].split(" ");
            x = parseInt(piecexy[0]);
            y = parseInt(piecexy[1]);
            squares[x][y].getPiece().setVisible(false);
            squares[x][y].setPiece(null);
        }
    }

    public void listenSocket() {
        try {
            socket = new Socket("localhost", 4000);
            // Inicjalizacja wysyłania do serwera
            OutputStream outStream = socket.getOutputStream();
            out = new PrintWriter(outStream, true);
            // Inicjalizacja odbierania od serwera
            InputStream inStream = socket.getInputStream();
            in = new BufferedReader(new InputStreamReader(inStream));
        }
        catch(IOException ex) {
            System.out.println("No I/O");
            System.exit(1);
        }
    }

    private void receiveInfoFromServer() {
        try {
            player = parseInt(in.readLine()); // ustawienie
            System.out.println("Witam z tej strony Player " + player);
            if(player == 1){
                yourTurn = true;
            }
        } catch (IOException e) {
            System.out.println("Read Failed");
            System.exit(1);
        }
    }

    private void startThread() {
        Thread gTh = new Thread(this);
        gTh.start();
    }

    public void initApp() {
        Stage stage = new Stage();
        Scene scene = new Scene(CreateBoard());
        stage.setTitle("Player " + player);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void start(Stage primaryStage) throws Exception { // wywołana wtedy, kiedy aplikacja JavaFX startuje
        listenSocket();
        receiveInfoFromServer();
        initApp();
        startThread();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void run() {
        playerMethod(player);
    }

    public void playerMethod(int player) {
        while(true) {
            synchronized(this) { // this is monitor object
                if (currentPlayer == player) {
                    try {
                        wait(5);
                    }
                    catch(InterruptedException e) {
                        System.out.println(e);
                    }
                }
                receiveMove();
            }
        }
    }

}