package com.example.checkers;

import java.lang.Object;
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
    ObjectOutputStream outObj = null;
    ObjectInputStream inObj = null;
    BufferedReader in = null;
    private int player;
    public final static int PLAYER1 = 1;
    public final static int PLAYER2 = 2;
    public static int ACTIVE = 0;
    public static final int NONACTIVE = 1;
    private static int currentPlayer = PLAYER1; // ustawienie obecnego gracza na gracza, który zaczyna białymi

    private static int showing = ACTIVE;
    String move = "";

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
                        squares[i][j].setPiece(piece);
                        pieceGroup.getChildren().add(piece);
                    }
                    else if(3 > j){
                        piece = new Piece(i,j,PieceType.WHITE);
                        squares[i][j].setPiece(piece);
                        pieceGroup.getChildren().add(piece);
                    }
                    piece.setOnMouseDragged(this);
                    piece.setOnMouseReleased(this);
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
        if(e.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            double dx = e.getX() - currPiece.getCenterX();
            double dy = e.getY() - currPiece.getCenterY();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if(currPiece.isHit(currPiece.getCenterX(), currPiece.getCenterY())) {
                        currPiece.setCenterX(currPiece.getCenterX() + dx);
                        currPiece.setCenterY(currPiece.getCenterY() + dy);
                    }
//                    piece.setCenterX(piece.getCenterX() + dx);
//                    piece.setCenterY(piece.getCenterY() + dy);
                }
            });
        }
        if(e.getEventType() == MouseEvent.MOUSE_RELEASED) {
            try {
                PrintWriter data_piece = new PrintWriter(socket.getOutputStream(), true);
                int new_coordinate_x = (int) (e.getX() - e.getX()%PieceSize)/PieceSize;
                int new_coordinate_y = (int) (e.getY() - e.getY()%PieceSize)/PieceSize;
                if(move == ""){
                    data_piece.println(currPiece.getX_pos() + " " + currPiece.getY_pos() + " -> " + new_coordinate_x + " " + new_coordinate_y);
                }
                /*
                System.out.println(move + "!!!");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if(move.equals("valid")) {
                            squares[currPiece.getX_pos()][currPiece.getY_pos()].setPiece(null);
                            currPiece.setCenterX(currPiece.getCenterX() - currPiece.getCenterX() % CheckersApp.PieceSize + CheckersApp.PieceSize * 0.5);
                            currPiece.setCenterY(currPiece.getCenterY() - currPiece.getCenterY() % CheckersApp.PieceSize + CheckersApp.PieceSize * 0.5);
                            currPiece.setOldX(currPiece.getCenterX() - currPiece.getCenterX() % CheckersApp.PieceSize + CheckersApp.PieceSize * 0.5);
                            currPiece.setOldY(currPiece.getCenterY() - currPiece.getCenterY() % CheckersApp.PieceSize + CheckersApp.PieceSize * 0.5);
                            squares[currPiece.getX_pos()][currPiece.getY_pos()].setPiece(currPiece);
                        } //else if (move.contains("move")) {
                            // move 1 2 -> 3 4
                            //System.out.println("Robi movseee");
                            //String[] date_xy = move.split(" ");
                            //squares[parseInt(date_xy[1])][parseInt(date_xy[2])].getPiece().setCenterX(parseInt(date_xy[4])*CheckersApp.PieceSize+CheckersApp.PieceSize*0.5);
                            //squares[parseInt(date_xy[1])][parseInt(date_xy[2])].getPiece().setCenterY(parseInt(date_xy[5])*CheckersApp.PieceSize+CheckersApp.PieceSize*0.5);
                        //}
                        else {
                            currPiece.setCenterX(currPiece.getOldX());
                            currPiece.setCenterY(currPiece.getOldY());
                        }
                    }
                });

                 */

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

    private void checkMoveCentre(MouseEvent e, Piece piece, int x_pos, int y_pos) {

    }

    private void receiveMove() {
        try {
            // Odebranie z serwera informacji o ruchu białych
            // Odebranie stanu gry i zmienienie gui
            move = in.readLine();
            System.out.println(move);
            if(move.contains("move")){
                change_position(move);
                move = "";
            }
            else if(move.contains("valid")) {
                change_position(move);
                move_position(move);
                move = "";
            }
            else if(move.contains("not")){
                back_position(move);

                move = "";
            }
            move="";
        } catch(IOException e) {
            System.out.println("Read failed");
            System.exit(1);
        }
    }
    private void back_position(String move){
        String[] date_xy = move.split(" ");
        Piece mypiece = squares[parseInt(date_xy[1])][parseInt(date_xy[2])].getPiece();
        System.out.println(date_xy[1] + " " + date_xy[2]);
        System.out.println(mypiece);
        mypiece.setCenterX(mypiece.getOldX());
        mypiece.setCenterY(mypiece.getOldY());
        System.out.println(squares[parseInt(date_xy[1])][parseInt(date_xy[2])].getPiece());
        //squares[parseInt(date_xy[1])][parseInt(date_xy[2])].setPiece(mypiece);
    }
    private void move_position(String move){
        String[] date_xy = move.split(" ");
        Piece mypiece = squares[parseInt(date_xy[1])][parseInt(date_xy[2])].getPiece();
        mypiece.setCenterX(mypiece.getCenterX() - mypiece.getCenterX() % CheckersApp.PieceSize + CheckersApp.PieceSize * 0.5);
        mypiece.setCenterY(mypiece.getCenterY() - mypiece.getCenterY() % CheckersApp.PieceSize + CheckersApp.PieceSize * 0.5);
        System.out.println(mypiece);
        squares[parseInt(date_xy[1])][parseInt(date_xy[2])].setPiece(null);
        mypiece.setOldX(mypiece.getCenterX() - mypiece.getCenterX() % CheckersApp.PieceSize + CheckersApp.PieceSize * 0.5);
        mypiece.setOldY(mypiece.getCenterY() - mypiece.getCenterY() % CheckersApp.PieceSize + CheckersApp.PieceSize * 0.5);
        squares[mypiece.getX_pos()][mypiece.getY_pos()].setPiece(mypiece);
    }
    private void change_position(String move){
        String[] date_xy = move.split(" ");
        Piece mypiece = squares[parseInt(date_xy[1])][parseInt(date_xy[2])].getPiece();
        mypiece.setCenterX(mypiece.getCenterX() - mypiece.getCenterX() % CheckersApp.PieceSize + CheckersApp.PieceSize * 0.5);
        mypiece.setCenterY(mypiece.getCenterY() - mypiece.getCenterY() % CheckersApp.PieceSize + CheckersApp.PieceSize * 0.5);
        System.out.println(mypiece);
        mypiece.setCenterX(parseInt(date_xy[4])*CheckersApp.PieceSize+CheckersApp.PieceSize*0.5);
        mypiece.setCenterY(parseInt(date_xy[5])*CheckersApp.PieceSize+CheckersApp.PieceSize*0.5);
    }

    public void listenSocket() {
        try {
            //System.out.println("Siema Eniu");
            socket = new Socket("localhost", 3000);
            // Inicjalizacja wysyłania do serwera
            OutputStream outStream = socket.getOutputStream();
            //outObj = new ObjectOutputStream(outStream);
            out = new PrintWriter(outStream, true);
            // Inicjalizacja odbierania od serwera
            InputStream inStream = socket.getInputStream();
            //inObj = new ObjectInputStream(inStream);
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

//        Task playerTask = new Task<Player>() { // asynchroniczne zadania
//            @Override
//            protected Player call() throws Exception {
//                player = new Player();
//                return player;
//            }
//        };
//        Thread th = new Thread(playerTask);
//        th.setDaemon(true);
//        th.start();

        Scene scene = new Scene(CreateBoard());
        stage.setTitle("Checkers");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void start(Stage primaryStage) throws Exception { // wywołana wtedy, kiedy aplikacja JavaFX startuje
        initApp();
        listenSocket();
        receiveInfoFromServer();
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
            synchronized(this) {
                if (currentPlayer == player) {
                    try {
                        wait(5);
                    }
                    catch(InterruptedException e) {
                        System.out.println(e);
                    }
                }
                //if(showing == ACTIVE) {
                    receiveMove();
                //    showing = NONACTIVE;
                //}
                //notifyAll();
            }
        }
    }

}