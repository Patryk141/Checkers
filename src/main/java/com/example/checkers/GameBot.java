package com.example.checkers;

import javafx.scene.input.MouseEvent;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Integer.parseInt;

/**
 * Server Class for sending the response to the clients
 * @author Patryk Piskorski Bartłomiej Puchała
 */
public class GameBot extends CheckersApp implements Runnable {

    private Socket firstPlayer;
    private boolean gameEnded = false;
    public static int size;
    private final static int FIRST=1; // Białe
    private final static int SECOND=2; // Czarne
    private static int turn=FIRST;
    private boolean checkMoves = false;
    private boolean checkMat = false;
    private boolean lock = true;
    private String line;
    private Square[][] squares;
    public String gameType;
    public GameRules rules = null;
    private Random rand;
    int blackPieces, whitePieces = 0;

    public GameBot(Socket firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    /**
     * Method for setting the squares object and initializing the GameRules instance
     */
    public void createBoard() {
        squares = new Square[size][size];

        for (int i=0 ; i<size ; i++) {
            for (int j=0 ; j<size ; j++) {
                squares[i][j] = new Square(i,j,(i+j)%2);
                if ((i+j)%2 != 0) {
                    if ( (size / 2) < j) {
                        piece = new Piece(i,j,PieceType.BLACK);
                        blackPieces++;
                        squares[i][j].setPiece(piece);
                    } else if (3 +((size - 8) / 2) > j) {
                        piece = new Piece(i,j,PieceType.WHITE);
                        whitePieces++;
                        squares[i][j].setPiece(piece);
                    }
                }
            }
        }
        if(gameType.equals("Russian")) {
            System.out.println(gameType);
            rules = new RussianGameRules();
        } else if(gameType.equals("Classic")) {
            System.out.println(gameType);
            rules = new ClassicGameRules();
        }
        rules.setNumberOfPieces(blackPieces, whitePieces);
    }
    /**
     * Thread method for initializing streams for each client, sending data to clients of their number, getting the data from the board and sending response to client
     */
    @Override
    public void run() {
        try {
            InputStream input_first = firstPlayer.getInputStream();
            BufferedReader in_first = new BufferedReader(new InputStreamReader(input_first));
            OutputStream output_first = firstPlayer.getOutputStream();
            PrintWriter out_first = new PrintWriter(output_first, true);
            out_first.println("1");

            String msgFromBoard = in_first.readLine();
            String[] info = msgFromBoard.split(" ");
            size = parseInt(info[0]);
            gameType = info[1];
            createBoard();

            do {
                if (turn == SECOND) { // Ruch czarnych
                    System.out.println("Turn Player 2");
                    rand = new Random();
                    if (lock) {
                        rules.availablePiece(turn, size, squares);
                    }
                    String allPiece = rules.getObligatoryPiece();
                    if (allPiece.contains("(")) {
                        String[] onePiece = allPiece.split(" \\( ");
                        int oldX, oldY;
                        int randomPiece = rand.nextInt(onePiece.length - 1);
                        String[] pieceXY = onePiece[randomPiece + 1].split(" ");
                        oldX = parseInt(pieceXY[0]);
                        oldY = parseInt(pieceXY[1]);
                        Piece botPiece = squares[oldX][oldY].getPiece();
                        String allMoves = rules.availableMoves(botPiece);
                        String[] oneMove = allMoves.split(" \\( ");
                        int newX, newY;
                        int randomMove = rand.nextInt(oneMove.length - 1);
                        String[] moveXY = oneMove[randomMove + 1].split(" ");
                        newX = parseInt(moveXY[0]);
                        newY = parseInt(moveXY[1]);
                        line = oldX + " " + oldY + " -> " + newX + " " + newY;
                        System.out.println(line);
                        int nextTurn = FIRST;
                        boolean classicCheck;
                        String chosenPiece = "( " + oldX + " " + oldY + " )";
                        if (rules.getAvailablePiece().contains(chosenPiece)) {
                            Piece tmpPiece = null;
                            lock = true;
                            if (rules.getObligatoryPiece().contains(chosenPiece)) {
                                checkMoves = rules.checkMove(oldX, oldY, newX, newY);
                                if (checkMoves) {
                                    if (rules.checkBlockedPieces(PieceType.BLACK)) {
                                        gameEnded = true;
                                        line = line + " WHITE WINS";
                                    } else if (rules.checkBlockedPieces(PieceType.WHITE)) {
                                        gameEnded = true;
                                        line = line + " BLACK WINS";
                                    }
                                    tmpPiece = squares[newX][newY].getPiece();
                                    checkMat = rules.checkIfMatted(oldX, oldY, newX, newY);
                                    if (gameType.equals("Classic")) {
                                        classicCheck = rules.availableMoves(tmpPiece).contains("kill");
                                    } else {
                                        classicCheck = false;
                                    }
                                    if (turn == 2 && newY == 0 && !classicCheck) {
                                        rules.promotion(newX, newY);
                                        line = line + " promotion";
                                    }
                                    if (turn == 1 && newY == size - 1 && !classicCheck) {
                                        line = line + " promotion";
                                        rules.promotion(newX, newY);
                                    }
                                }
                                if (checkMoves && checkMat) {
                                    int nrBlackPieces = rules.getBlackPieces();
                                    int nrWhitePieces = rules.getWhitePieces();
                                    if (nrBlackPieces == 0 && nrWhitePieces > 0) {
                                        gameEnded = true;
                                        line = line + " WHITE WINS";
                                    } else if (nrWhitePieces == 0 && nrBlackPieces > 0) {
                                        gameEnded = true;
                                        line = line + " BLACK WINS";
                                    }
                                    if (rules.availableMoves(tmpPiece).contains("kill")) { // check for double matting
                                        out_first.println("valid " + line + " matted turn");
                                        ;
                                        lock = false;
                                        String msg_ = " ( " + newX + " " + newY + " )";
                                        rules.setAvailablePiece(msg_);
                                        rules.setObligatoryPiece(msg_);
                                    } else { // single matting
                                        out_first.println("valid " + line + " matted");
                                        turn = nextTurn;
                                    }
                                } else if (checkMoves) {
                                    out_first.println("valid " + line);
                                    turn = nextTurn;
                                } else {
                                    out_first.println("not " + line);
                                }
                            } else {
                                out_first.println("not " + line);
                            }
                        } else {
                            out_first.println("not " + line);
                        }
                    } else {
                        out_first.println("valid WHITE WINS");
                        gameEnded = true;
                    }
                }
                if (turn == FIRST) { // Ruch białych
                    System.out.println("Turn Player 1");
                    int nextTurn = SECOND;
                    try {
                        line = in_first.readLine();
                        System.out.println(line);
                        String[] data_piece = line.split(" ");
                        int oldX = parseInt(data_piece[0]);
                        int oldY = parseInt(data_piece[1]);
                        int newX = parseInt(data_piece[3]);
                        int newY = parseInt(data_piece[4]);
                        boolean classicCheck;
                        String chosenPiece =  "( " + oldX + " " + oldY + " )";
                        if (lock) {
                            rules.availablePiece(turn, size, squares);
                        }
                        if (rules.getAvailablePiece().contains(chosenPiece)) {
                            Piece tmpPiece = null;
                            lock = true;
                            if (rules.getObligatoryPiece().contains(chosenPiece)) {
                                checkMoves = rules.checkMove(oldX, oldY, newX, newY);
                                if (checkMoves) {
                                    if(rules.checkBlockedPieces(PieceType.BLACK)) {
                                        gameEnded = true;
                                        line = line + " WHITE WINS";
                                    } else if(rules.checkBlockedPieces(PieceType.WHITE)) {
                                        gameEnded = true;
                                        line = line + " BLACK WINS";
                                    }
                                    tmpPiece = squares[newX][newY].getPiece();
                                    checkMat = rules.checkIfMatted(oldX, oldY, newX, newY);
                                    if(gameType.equals("Classic")) {
                                        classicCheck = rules.availableMoves(tmpPiece).contains("kill");
                                    } else {
                                        classicCheck = false;
                                    }
                                    if (turn == 2 && newY == 0 && !classicCheck) {
                                        rules.promotion(newX, newY);
                                        line = line + " promotion";
                                    }
                                    if(turn == 1 && newY == size - 1 && !classicCheck) {
                                        line = line + " promotion";
                                        rules.promotion(newX, newY);
                                    }
                                }
                                if (checkMoves && checkMat) {
                                    int nrBlackPieces = rules.getBlackPieces();
                                    int nrWhitePieces = rules.getWhitePieces();
                                    if(nrBlackPieces == 0 && nrWhitePieces > 0) {
                                        gameEnded = true;
                                        line = line + " WHITE WINS";
                                    } else if(nrWhitePieces == 0 && nrBlackPieces > 0) {
                                        gameEnded = true;
                                        line = line + " BLACK WINS";
                                    }
                                    if (rules.availableMoves(tmpPiece).contains("kill")) { // check for double matting
                                        out_first.println("valid " + line + " matted turn");
                                        lock = false;
                                        String msg_ = " ( " + newX + " " + newY + " )";
                                        rules.setAvailablePiece(msg_);
                                        rules.setObligatoryPiece(msg_);
                                    } else { // single matting
                                        out_first.println("valid " + line + " matted");
                                        turn = nextTurn;
                                    }
                                } else if (checkMoves) {
                                    out_first.println("valid " + line);
                                    turn = nextTurn;
                                } else {
                                    out_first.println("not " + line);
                                }
                            } else {
                                out_first.println("not " + line);
                            }
                        } else {
                            out_first.println("not " + line);
                        }
                    }
                    catch(IOException err) {
                        err.printStackTrace();
                    }
                }
            } while (!gameEnded);

        } catch (IOException ex) {
            System.err.println("ex");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}