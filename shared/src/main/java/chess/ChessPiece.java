package chess;

import chess.calculator.*;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return switch (type) {
            case PieceType.BISHOP -> new BishopMovesCalculator(board, myPosition).pieceMoves();
            case PieceType.ROOK -> new RookMovesCalculator(board, myPosition).pieceMoves();
            case PieceType.QUEEN -> new QueenMovesCalculator(board, myPosition).pieceMoves();
            case PieceType.KING -> new KingMovesCalculator(board, myPosition).pieceMoves();
            case PieceType.KNIGHT -> new KnightMovesCalculator(board, myPosition).pieceMoves();
            case PieceType.PAWN -> new PawnMovesCalculator(board, myPosition).pieceMoves();
        };
    }

    private String toStringHelper(ChessGame.TeamColor color, PieceType type) {
        String letter = switch (type) {
            case PAWN -> "P";
            case ROOK -> "R";
            case KNIGHT -> "N";
            case BISHOP -> "B";
            case QUEEN -> "Q";
            case KING -> "K";
        };
        return (color == ChessGame.TeamColor.WHITE) ? letter : letter.toLowerCase();
    }

    @Override
    public String toString() {
        return String.format("%s",toStringHelper(pieceColor, type));
    }
}
