package com.example.checkers;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

/**
 * Class for creating the checkers board, connecting to server, handling server response and updating gui
 * @author Bartłomiej Puchała Patryk Piskorski
 */
public class CheckersApp extends Application implements Runnable, EventHandler<MouseEvent> {

    public static int PieceSize;
    Piece piece;
    public static Square[][] squares;

    Socket socket = null;
    PrintWriter out = null;
    BufferedReader in = null;
    private int player;
    public final static int PLAYER1 = 1;
    public final static int PLAYER2 = 2;
    public static int ACTIVE = 0;
    private static int currentPlayer = PLAYER1; // ustawienie obecnego gracza na gracza, który zaczyna białymi

    private String WINNER = "";
    private static int showing = ACTIVE;
    private boolean yourTurn = false;
    private String move = "";
    private int new_coordinate_x, new_coordinate_y;

    public int getPlayer() {
        return player;
    }

    public Square[][] getSquares() {
        return squares;
    }

    /**
     * Method for handling the events on pieces, changing gui based on these events and sending information about the move to server
     * @param e - MouseEvent object
     */
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
                    System.out.println(currPiece.getX_pos() + " " + currPiece.getY_pos() + " -> " + new_coordinate_x + " " + new_coordinate_y);
                    dataPiece.println(currPiece.getX_pos() + " " + currPiece.getY_pos() + " -> " + new_coordinate_x + " " + new_coordinate_y);
                }
                else {
                    currPiece.setCenterX(currPiece.getOldX());
                    currPiece.setCenterY(currPiece.getOldY());
                }
            } catch(IOException ex) {
                System.out.println("error");
                ex.printStackTrace();
            }
            showing = ACTIVE;
            currentPlayer = player;
        }
    }

    /**
     * Method for creating alert notification after some player wins the game
     */
    private void communicate() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("End of Game");
                alert.setHeaderText(null);
                alert.setContentText("The winner is " + WINNER);
                alert.showAndWait();
            }
        });
    }

    /**
     * Method for handling the response from the server and calling appropriate functions which changes gui
     */
    private void receiveMove() { // both player 1 and player 2 call this method after one of them makes move
        try {
            // Odebranie stanu gry i zmienienie gui
            move = in.readLine();
            System.out.println(move);
            if (move.contains("remove")) {
                remove(move);
            }
            if (move.contains("valid")) {
                if(move.contains("WHITE WINS")) {
                    WINNER = "White";
                    communicate();
                }
                if(move.contains("BLACK WINS")) {
                    WINNER = "Black";
                    communicate();
                }
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

    /**
     * Method for creating the king(changing piece to king)
     * @param move - message returned by the server
     */
    private void promotionPiece(String move) {
        String[] date_xy = move.split(" ");
        squares[parseInt(date_xy[1])][parseInt(date_xy[2])].getPiece().setKing();
    }

    /**
     * Method for returning the piece back(setting old coordinates) if the move is not valid
     * @param move - message returned by the server
     */
    protected void backPosition(String move){
        String[] date_xy = move.split(" ");
        Piece mypiece = squares[parseInt(date_xy[1])][parseInt(date_xy[2])].getPiece();
        mypiece.setCenterX(mypiece.getOldX());
        mypiece.setCenterY(mypiece.getOldY());
    }

    /**
     * Method for changing the coordinates of the piece if the move was valid and updating the squares object
     * @param move - message returned by the server
     */
    protected void changePosition(String move) {
        String[] date_xy = move.split(" ");
        Piece mypiece = squares[parseInt(date_xy[1])][parseInt(date_xy[2])].getPiece();
        mypiece.setCenterX(parseInt(date_xy[4])*PieceSize+PieceSize*0.5);
        mypiece.setCenterY(parseInt(date_xy[5])*PieceSize+PieceSize*0.5);
        mypiece.setX_pos(parseInt(date_xy[4])); // setting new x_pos
        mypiece.setY_pos(parseInt(date_xy[5])); // setting new y_pos
        mypiece.setOldX(mypiece.getCenterX() - mypiece.getCenterX() % PieceSize + PieceSize * 0.5);
        mypiece.setOldY(mypiece.getCenterY() - mypiece.getCenterY() % PieceSize + PieceSize * 0.5);
        squares[parseInt(date_xy[1])][parseInt(date_xy[2])].setPiece(null); // setting old piece to null
        squares[mypiece.getX_pos()][mypiece.getY_pos()].setPiece(mypiece); // setting new piece
    }

    /**
     * Method for removing the matted piece from the gui if the other piece matted
     * @param move - message returned by the server
     */
    protected void removePiece(String move) {
        String[] date_xy = move.split(" ");

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

    /**
     * Method for removing the piece from the board
     * @param move - message returned by the server
     */
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

    /**
     * Method for creating the connection to the server(socket obj) and initializing streams
     */
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

    /**
     * Method for receiving info about the player number from the server
     */
    public void receiveInfoFromServer() {
        try {
            player = parseInt(in.readLine()); // ustawienie
            if(player == 1){
                yourTurn = true;
            }
        } catch (IOException e) {
            System.out.println("Read Failed");
            System.exit(1);
        }
    }

    /**
     * Method for starting the thread
     */
    public void startThread() {
        Thread gTh = new Thread(this);
        gTh.start();
    }

    /**
     * Method for adding handlers to whitePieces for player1 and blackPieces to player2
     * @param blackPieces - array list of blackPieces
     * @param whitePieces - array list of whitePieces
     */
    private void initHandlers(ArrayList<Piece> blackPieces, ArrayList<Piece> whitePieces) {
        for(Piece blackPiece: blackPieces) {
            if(player == PLAYER2) {
                blackPiece.setOnMouseDragged(this);
                blackPiece.setOnMouseReleased(this);
            }
        }
        for(Piece whitePiece: whitePieces) {
            if(player == PLAYER1) {
                whitePiece.setOnMouseDragged(this);
                whitePiece.setOnMouseReleased(this);
            }
        }
    }

    /**
     * Method is calling the methods for creating gui(board, pieces, squares), connecting to server itp.
     * Method is called from the CheckersMenu class
     * @param board - type of board specific to each
     * @param stage - stage object from the CheckersMenu class
     * @link{ com.example.checkers.CheckersMenu }
     */
    public void initApp(CheckersBoard board, Stage stage, boolean bot) { // Abstract Factory
        listenSocket();
        out.println(bot);
        receiveInfoFromServer();
        board.sendMsgToServer(socket);


        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                stage.close();
                Stage newStage = new Stage();
                PieceSize = board.getPieceSize();
                Scene scene = new Scene(board.createBoard());
                squares = board.getSquares();
                initHandlers(board.getBlackPieces(), board.getWhitePieces()); // activating handlers
                newStage.setTitle("Player " + player);
                newStage.setScene(scene);
                newStage.show();
            }
        });
        startThread();
    }

    @Override
    public void start(Stage primaryStage) throws Exception { // wywołana wtedy, kiedy aplikacja JavaFX startuje

    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void run() {
        playerMethod(player);
    }

    /**
     * Method for synchronization and calling the method to handle the response from the server
     * @param player - current player which has move
     */
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