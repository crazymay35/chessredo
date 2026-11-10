package client;

import chess.*;

import static ui.EscapeSequences.*;

public class ClientMain {
    public static void main(String[] args) {
        //var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        //System.out.println("♕ 240 Chess Client: " + piece);
        var game = new ChessGame();
        ClientMain clientMain = new ClientMain();
        clientMain.printChessboard(game.getBoard());
        System.out.print(SET_BG_COLOR_WHITE);
        System.out.print(SET_TEXT_COLOR_BLACK);
        System.out.print(SET_TEXT_BOLD);
    }
    public void printChessboard(ChessBoard board) {
        for (int row = 8; row > 0; row--) {
            for (int col = 1; col < 10; col++) {
                if (col == 9) {
                    System.out.print(SET_BG_COLOR_BLACK);
                    break;
                }
                if ((row + col) % 2 == 0) {
                    System.out.print(SET_BG_COLOR_LIGHT_GREY);
                } else {
                    System.out.print(SET_BG_COLOR_WHITE);
                }
                var pos = new ChessPosition(row,col);
                if (board.getPiece(pos) != null) {
                    printPiece(board.getPiece(pos));
                }
                else {
                    System.out.print(EMPTY);
                }
            }
            System.out.print('\n');
        }
    }
    private void printPiece(ChessPiece piece) {
        String pieceType = switch (piece.getPieceType()) {
            case ROOK -> piece.getTeamColor() == ChessGame.TeamColor.BLACK ? BLACK_ROOK : WHITE_ROOK;
            case KNIGHT -> piece.getTeamColor() == ChessGame.TeamColor.BLACK ? BLACK_KNIGHT : WHITE_KNIGHT;
            case BISHOP -> piece.getTeamColor() == ChessGame.TeamColor.BLACK ? BLACK_BISHOP : WHITE_BISHOP;
            case QUEEN -> piece.getTeamColor() == ChessGame.TeamColor.BLACK ? BLACK_QUEEN : WHITE_QUEEN;
            case KING -> piece.getTeamColor() == ChessGame.TeamColor.BLACK ? BLACK_KING : WHITE_KING;
            case PAWN -> piece.getTeamColor() == ChessGame.TeamColor.BLACK ? BLACK_PAWN : WHITE_PAWN;
        };
        System.out.print(pieceType);
    }
}
