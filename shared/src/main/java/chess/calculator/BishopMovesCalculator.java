package chess.calculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator extends PieceMovesCalculator {
    public BishopMovesCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public Collection<ChessMove> pieceMoves() {
        Collection<ChessMove> allMoves = new ArrayList<>();
        allMoves.addAll(moveDirection(Direction.UP_RIGHT,8));
        allMoves.addAll(moveDirection(Direction.DOWN_RIGHT,8));
        allMoves.addAll(moveDirection(Direction.DOWN_LEFT,8));
        allMoves.addAll(moveDirection(Direction.UP_LEFT,8));
        return allMoves;
    }
}
