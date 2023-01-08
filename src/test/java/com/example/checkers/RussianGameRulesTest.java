package com.example.checkers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RussianGameRulesTest {
    private Square[][] squares = null;
    RussianGameRules russianGameRules = new RussianGameRules();
    Piece pieceWhite1 = new Piece(3, 0, PieceType.WHITE);
    Piece pieceWhite2 = new Piece(2, 1, PieceType.WHITE);
    Piece pieceWhite3 = new Piece(4, 1, PieceType.WHITE);
    Piece pieceWhite4 = new Piece(0, 5, PieceType.WHITE);
    Piece pieceBlack1 = new Piece(1, 2, PieceType.BLACK);
    Piece pieceBlack2 = new Piece(3, 2, PieceType.BLACK);
    Piece pieceBlack3 = new Piece(3, 4, PieceType.BLACK);
    Piece pieceBlack4 = new Piece(1, 6, PieceType.BLACK);
    @BeforeEach
    void createBoard() {
        squares = new Square[8][8];
        for (int i=0 ; i<8 ; i++) {
            for (int j=0 ; j<8 ; j++) {
                squares[i][j] = new Square(i, j, (i+j)%2);
            }
        }
        squares[3][0].setPiece(pieceWhite1);
        squares[2][1].setPiece(pieceWhite2);
        squares[4][1].setPiece(pieceWhite3);
        squares[0][5].setPiece(pieceWhite4);
        squares[1][2].setPiece(pieceBlack1);
        squares[3][2].setPiece(pieceBlack2);
        squares[3][4].setPiece(pieceBlack3);
        squares[1][6].setPiece(pieceBlack4);
        russianGameRules.availablePiece(1, 8, squares);
    }

    @Test
    void availablePiece() {
        String myPiece = " ( 0 5 ) ( 2 1 ) ( 4 1 )";
        assertEquals(russianGameRules.getObligatoryPiece(), myPiece);
    }

    @Test
    void availableMove() {
        assertEquals(russianGameRules.availableMoves(pieceWhite1), "");
        String myMoves2 = " kill ( 0 3 ) kill ( 4 3 )";
        assertEquals(russianGameRules.availableMoves(pieceWhite2), myMoves2);
    }

    @Test
    void checkIfMatted() {
        assertEquals(russianGameRules.checkIfMatted(0, 5, 2, 7), true);
        assertEquals(russianGameRules.checkIfMatted(2, 1, 0, 3), true);
        assertEquals(russianGameRules.checkIfMatted(4, 1, 5, 2), false);
    }

    @Test
    void checkMove() {
        assertEquals(russianGameRules.checkMove(3, 0, 2, 1), false);
        assertEquals(russianGameRules.checkMove(3, 4,2, 3), true);
    }

    @Test
    void checkBlockedPieces() {
        assertEquals(russianGameRules.checkBlockedPieces(PieceType.WHITE), false);
        assertEquals(russianGameRules.checkBlockedPieces(PieceType.BLACK), false);
    }
}