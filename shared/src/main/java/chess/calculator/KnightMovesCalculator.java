package chess.calculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator extends PieceMovesCalculator {
    public KnightMovesCalculator(ChessBoard board, ChessPosition position) {
        super(board,position);
    }

    @Override
    public Collection<ChessMove> pieceMoves() {
        Collection<ChessMove> allMoves = new ArrayList<>();
        allMoves.addAll(moveDirection(Direction.K_UP_RIGHT,1));
        allMoves.addAll(moveDirection(Direction.K_UP_LEFT,1));
        allMoves.addAll(moveDirection(Direction.K_RIGHT_UP,1));
        allMoves.addAll(moveDirection(Direction.K_RIGHT_DOWN,1));
        allMoves.addAll(moveDirection(Direction.K_DOWN_RIGHT,1));
        allMoves.addAll(moveDirection(Direction.K_DOWN_LEFT,1));
        allMoves.addAll(moveDirection(Direction.K_LEFT_UP,1));
        allMoves.addAll(moveDirection(Direction.K_LEFT_DOWN,1));
        return allMoves;
    }
}
