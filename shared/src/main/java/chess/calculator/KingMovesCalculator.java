package chess.calculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator extends PieceMovesCalculator {

    public KingMovesCalculator(ChessBoard board, ChessPosition position) {
        super(board,position);
    }

    @Override
    public Collection<ChessMove> pieceMoves() {
        Collection<ChessMove> allMoves = new ArrayList<>();
        allMoves.addAll(moveDirection(Direction.UP,1));
        allMoves.addAll(moveDirection(Direction.UP_RIGHT,1));
        allMoves.addAll(moveDirection(Direction.RIGHT,1));
        allMoves.addAll(moveDirection(Direction.DOWN_RIGHT,1));
        allMoves.addAll(moveDirection(Direction.DOWN,1));
        allMoves.addAll(moveDirection(Direction.DOWN_LEFT,1));
        allMoves.addAll(moveDirection(Direction.LEFT,1));
        allMoves.addAll(moveDirection(Direction.UP_LEFT,1));
        return allMoves;
    }
}
