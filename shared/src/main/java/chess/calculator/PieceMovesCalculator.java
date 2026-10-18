package chess.calculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public abstract class PieceMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition position;
    protected PieceMovesCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }
    protected ChessBoard getBoard() {
        return board;
    }
    protected ChessPosition getPosition() {
        return position;
    }
    protected boolean inBounds(ChessPosition position) {
        return position.getRow() >=1 && position.getRow() <= 8 && position.getColumn() >= 1 && position.getColumn() <= 8;
    }
    protected boolean pieceIsNull(ChessPosition position) {
        return board.getPiece(position) == null;
    }
    protected boolean teamColorNotMatch(ChessPiece newPiece, ChessPiece myPiece) {
        return newPiece.getTeamColor() != myPiece.getTeamColor();
    }
    protected enum Direction {
        UP(1,0),
        UP_RIGHT(1,1),
        RIGHT(0,1),
        DOWN_RIGHT(-1,1),
        DOWN(-1,0),
        DOWN_LEFT(-1,-1),
        LEFT(0,-1),
        UP_LEFT(1,-1),

        K_UP_RIGHT(2,1),
        K_UP_LEFT(2,-1),
        K_RIGHT_UP(1,2),
        K_RIGHT_DOWN(-1,2),
        K_DOWN_RIGHT(-2,1),
        K_DOWN_LEFT(-2,-1),
        K_LEFT_UP(1,-2),
        K_LEFT_DOWN(-1,-2),

        FIRST_MOVE(2,0),
        FORWARD(1,0),
        ATTACK_RIGHT(1,1),
        ATTACK_LEFT(1,-1);

        private final int row;
        private final int col;

        Direction(int row, int col) {
            this.row = row;
            this.col = col;
        }
        public ChessPosition step(ChessPosition position, int length) {
            return new ChessPosition(position.getRow() + (this.row * length), position.getColumn() + (this.col * length));
        }
    }
    public abstract Collection<ChessMove> pieceMoves();
    protected Collection<ChessMove> moveDirection(Direction direction, int max) {
        ChessPiece piece = board.getPiece(position);
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        int i = 1;
        while (inBounds(direction.step(position, i))&& i <= max) {
            ChessPosition newPosition = direction.step(position, i);
            ChessPiece newPiece = board.getPiece(newPosition);
            if (pieceIsNull(newPosition)) {
                possibleMoves.add(new ChessMove(position, newPosition, null));
            }
            else if(teamColorNotMatch(newPiece, piece)) {
                possibleMoves.add(new ChessMove(position, newPosition, null));
                break;
            }
            else { break; }
            i++;
        }
        return possibleMoves;
    }
}
