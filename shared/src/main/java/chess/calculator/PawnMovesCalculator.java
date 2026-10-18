package chess.calculator;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator extends PieceMovesCalculator{
    public PawnMovesCalculator (ChessBoard board, ChessPosition position) {
        super(board,position);
    }

    private void possiblePromotions(Collection<ChessMove> possibleMoves, ChessPosition newPosition, int rowPiecePromote) {
        if (getPosition().getRow() == rowPiecePromote) {
            possibleMoves.add(new ChessMove(getPosition(),newPosition, ChessPiece.PieceType.QUEEN));
            possibleMoves.add(new ChessMove(getPosition(),newPosition,ChessPiece.PieceType.ROOK));
            possibleMoves.add(new ChessMove(getPosition(),newPosition,ChessPiece.PieceType.BISHOP));
            possibleMoves.add(new ChessMove(getPosition(),newPosition,ChessPiece.PieceType.KNIGHT));
        }
        else {
            possibleMoves.add(new ChessMove(getPosition(),newPosition,null));
        }
    }

    private void forward(Collection<ChessMove> possibleMoves, int rowPiecePromote, int direction) {
        ChessPosition forward = Direction.FORWARD.step(getPosition(),direction);
        if (inBounds(forward) && pieceIsNull(forward)) {
            possiblePromotions(possibleMoves,forward,rowPiecePromote);
        }
    }
    private void firstMove(Collection<ChessMove> possibleMoves, int rowPiecePromote, int direction) {
        ChessPosition firstMove = Direction.FIRST_MOVE.step(getPosition(),direction);
        ChessPosition forward = Direction.FORWARD.step(getPosition(),direction);
        if (getPosition().getRow() == rowPiecePromote && pieceIsNull(forward) && pieceIsNull(firstMove)) {
            possibleMoves.add(new ChessMove(getPosition(),firstMove,null));
        }
    }
    private void attack(Collection<ChessMove> possibleMoves, int rowPiecePromote, ChessPosition attackPosition) {
        ChessPiece piece = getBoard().getPiece(getPosition());
        if (inBounds(attackPosition) && !pieceIsNull(attackPosition)) {
            ChessPiece attackedRightPiece = getBoard().getPiece(attackPosition);
            if (teamColorNotMatch(piece, attackedRightPiece)) {
                possiblePromotions(possibleMoves,attackPosition,rowPiecePromote);
            }
        }
    }

    private Collection<ChessMove> moveDirection(int dir, int start, int end) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        forward(possibleMoves,end,dir);
        firstMove(possibleMoves,start,dir);
        ChessPosition attackRight = Direction.ATTACK_RIGHT.step(getPosition(),dir);
        attack(possibleMoves,end, attackRight);
        ChessPosition attackLeft = Direction.ATTACK_LEFT.step(getPosition(),dir);
        attack(possibleMoves,end, attackLeft);
        return possibleMoves;
    }
    @Override
    public Collection<ChessMove> pieceMoves() {
        ChessPiece piece = getBoard().getPiece(getPosition());
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return moveDirection(1,2,7);
        }
        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            return moveDirection(-1,7,2);
        }
        return new ArrayList<>();
    }
}
