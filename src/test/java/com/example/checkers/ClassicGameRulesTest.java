package com.example.checkers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClassicGameRulesTest {
    private Square[][] squares = null;
    ClassicGameRules classicGameRules = new ClassicGameRules();
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
        classicGameRules.availablePiece(1, 8, squares);
    }
    @Test
    void availablePiece() {
        String myPiece = " ( 2 1 )";
        assertEquals(classicGameRules.getObligatoryPiece(), myPiece);
    }

    @Test
    void availableMoves() {
        assertEquals(classicGameRules.availableMoves(pieceWhite1), "");
        String myMoves2 = " kill ( 0 3 ) kill ( 4 3 )";
        assertEquals(classicGameRules.availableMoves(pieceWhite2), myMoves2);
    }

    @Test
    void howManyMat() {
        assertEquals(classicGameRules.howManyMat(pieceWhite1, 0, -1, -1), 0);
        assertEquals(classicGameRules.howManyMat(pieceWhite2, 0, -1, -1), 3);
        assertEquals(classicGameRules.howManyMat(pieceWhite3, 0, -1, -1), 2);
        assertEquals(classicGameRules.howManyMat(pieceWhite4, 0, -1, -1), 1);
    }

    @Test
    void checkIfMatted() {
        assertEquals(classicGameRules.checkIfMatted(0, 5, 2, 7), true);
        assertEquals(classicGameRules.checkIfMatted(2, 1, 0, 3), true);
        assertEquals(classicGameRules.checkIfMatted(4, 1, 5, 2), false);
    }

    @Test
    void checkMove() {
        assertEquals(classicGameRules.checkMove(3, 0, 2, 1), false);
        assertEquals(classicGameRules.checkMove(3, 4,2, 3), true);
    }

    @Test
    void checkBlockedPieces() {
        assertEquals(classicGameRules.checkBlockedPieces(PieceType.WHITE), false);
        assertEquals(classicGameRules.checkBlockedPieces(PieceType.BLACK), false);
    }
}