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

public class CheckersApp extends Application implements Runnable, EventHandler<MouseEvent> {

    public static final int PieceSize = 80;
//    private Player player;
    private final int Size = 8;
    int i, j;
    Piece piece;
    private final Square[][] squares = new Square[Size][Size];

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
                        pieceGroup.getChildren().add(piece);
                    }
                    else if(3 > j){
                        piece = new Piece(i,j,PieceType.WHITE);
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
                System.out.println(currPiece);
                currPiece.createCircle();
//                Circle circle = new Circle(currPiece.getCenterX(), currPiece.getCenterY(), currPiece.getRadius(), currPiece.getPaint());
                outObj.writeObject(currPiece);
//                outObj.writeObject(new Circle(currPiece.getCenterX(), currPiece.getCenterY(), currPiece.getRadius(), currPiece.getPaint())); // sending a piece object to check validity of move to server

                InputStream newInput = socket.getInputStream();
                BufferedReader new_reader = new BufferedReader(new InputStreamReader(newInput));

                String msg = new_reader.readLine();
                System.out.println(msg);
                System.out.println("Answer is " + msg);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if(msg.equals("valid")) {
                            currPiece.setCenterX(currPiece.getCenterX() - currPiece.getCenterX()%CheckersApp.PieceSize + CheckersApp.PieceSize*0.5);
                            currPiece.setCenterY(currPiece.getCenterY() - currPiece.getCenterY()%CheckersApp.PieceSize + CheckersApp.PieceSize*0.5);
                            currPiece.setOldX(currPiece.getCenterX() - currPiece.getCenterX()%CheckersApp.PieceSize + CheckersApp.PieceSize*0.5);
                            currPiece.setOldY(currPiece.getCenterY() - currPiece.getCenterY()%CheckersApp.PieceSize + CheckersApp.PieceSize*0.5);
                        } else {
                            currPiece.setCenterX(currPiece.getOldX());
                            currPiece.setCenterY(currPiece.getOldY());
                        }
                    }
                });
            } catch(IOException ex) {
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
            String move = in.readLine();
            System.out.println(move);
        } catch(IOException e) {
            System.out.println("Read failed");
            System.exit(1);
        }
    }


    public void listenSocket() {
        try {
            System.out.println("Siema Eniu");
            socket = new Socket("localhost", 3000);
            // Inicjalizacja wysyłania do serwera
            OutputStream outStream = socket.getOutputStream();
            outObj = new ObjectOutputStream(outStream);
            out = new PrintWriter(outStream, true);
            // Inicjalizacja odbierania od serwera
            InputStream inStream = socket.getInputStream();
            inObj = new ObjectInputStream(inStream);
            in = new BufferedReader(new InputStreamReader(inStream));
        }
        catch(IOException ex) {
            System.out.println("No I/O");
            System.exit(1);
        }
    }

    private void receiveInfoFromServer() {
        try {
            player = Integer.parseInt(in.readLine()); // ustawienie
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
                        wait(10);
                    }
                    catch(InterruptedException e) {
                        System.out.println(e);
                    }
                }
                if(showing == ACTIVE) {
                    receiveMove();
                    showing = NONACTIVE;
                }
                notifyAll();
            }
        }
    }

}