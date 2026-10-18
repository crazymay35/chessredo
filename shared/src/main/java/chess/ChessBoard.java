package chess;

import java.util.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    private final ChessPiece[][] board;
    public ChessBoard() {
        this.board = new ChessPiece[8][8];
    }

    public ChessBoard(ChessBoard other) {
        this.board = new ChessPiece[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (other.board[row][col] != null) {
                    this.board[row][col] = other.board[row][col];
                }
            }
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        Collection<ChessPiece> whitePieces = createPieces(ChessGame.TeamColor.WHITE);
        addToBoard(whitePieces,1);
        addPawn(ChessGame.TeamColor.WHITE, 2);

        Collection<ChessPiece> blackPieces = createPieces(ChessGame.TeamColor.BLACK);
        addToBoard(blackPieces,8);
        addPawn(ChessGame.TeamColor.BLACK, 7);
    }

    private Collection<ChessPiece> createPieces(ChessGame.TeamColor color) {
        Collection<ChessPiece> pieces = new ArrayList<>();
        pieces.add(new ChessPiece(color,ChessPiece.PieceType.ROOK));
        pieces.add(new ChessPiece(color,ChessPiece.PieceType.KNIGHT));
        pieces.add(new ChessPiece(color,ChessPiece.PieceType.BISHOP));
        pieces.add(new ChessPiece(color,ChessPiece.PieceType.QUEEN));
        pieces.add(new ChessPiece(color,ChessPiece.PieceType.KING));
        pieces.add(new ChessPiece(color,ChessPiece.PieceType.BISHOP));
        pieces.add(new ChessPiece(color,ChessPiece.PieceType.KNIGHT));
        pieces.add(new ChessPiece(color,ChessPiece.PieceType.ROOK));
        return pieces;
    }
    private void addPawn(ChessGame.TeamColor teamColor, int row) {
        for (int i = 1; i <= 8; i++) {
            addPiece(new ChessPosition(row,i),new ChessPiece(teamColor,ChessPiece.PieceType.PAWN));
        }
    }
    private void addToBoard(Collection<ChessPiece> pieces, int row) {
        int column = 1;
        for (ChessPiece piece : pieces) {
            addPiece(new ChessPosition(row,column),piece);
            column++;
        }
    }

    @Override
    public String toString() {
        StringBuilder board = new StringBuilder();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (this.board[row][col] == null) {
                    board.append("| ");
                }
                else {
                    board.append("|");
                    board.append(this.board[row][col]);
                }
            }
            board.append("|\n");
        }
        return board.toString();
    }
}
