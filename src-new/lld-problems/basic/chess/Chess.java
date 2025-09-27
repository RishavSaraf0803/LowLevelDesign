package Chess;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
//Queue is a Interface
public class Chess extends BoardGame {
	
	private final ChessPlayer player1, player2;
    private final ChessBoard chessBoard;
	
	public Chess(ChessBoard chessBoard, ChessPlayer player1, ChessPlayer player2) {
		super(chessBoard, new ArrayDeque<Player>(Arrays.asList(player1,player2)));
		this.player1 = player1;
		this.player2 = player2;
		this.chessBoard = chessBoard;
		//chessBoard.prepareBoard(player1, player2,chessBoard);
		this.prepareBoard(player1, player2, this.chessBoard);
		//chessBoard.display();
	}

	private void prepareBoard(ChessPlayer player1, ChessPlayer player2, ChessBoard chessBoard) {
		placePawns(1, chessBoard, player1);
		placePawns(6, chessBoard, player2);
		placeKnight(0, chessBoard, player1);
		placeKnight(0, chessBoard, player1);
		placeKnight(7, chessBoard, player2);
		placeRook(0, chessBoard, player1);
		placeRook(7, chessBoard, player2);
		placeKing(0, chessBoard, player1);
		placeKing(7, chessBoard, player2);
		placeQueen(0, chessBoard, player1);
		placeQueen(7, chessBoard, player2);
		placeBishop(0, chessBoard, player1);
		placeBishop(7, chessBoard, player2);
		
		
	}
	
	private void placePawns(int row, ChessBoard board, ChessPlayer player) {
		List<PieceName> pieces = Arrays.asList(PieceName.PAWN1, PieceName.PAWN2,PieceName.PAWN3,PieceName.PAWN4, PieceName.PAWN5,PieceName.PAWN6,PieceName.PAWN7,PieceName.PAWN8);
		int col = 0;
		for(PieceName pieceName: pieces) {
			board.putPiece(player.getPiece(pieceName), row,col++);
		}
	}
	private void placeKnight(int row, ChessBoard board, ChessPlayer player) {
		int col1 = 1, col2 = 6;
		board.putPiece(player.getPiece(PieceName.KNIGHT1), row, col1);

		board.putPiece(player.getPiece(PieceName.KNIGHT2), row, col2);
	}
	private void placeRook(int row, ChessBoard board, ChessPlayer player) {
		int col1 = 0, col2 = 7;
		board.putPiece(player.getPiece(PieceName.ROOK1), row, col1);

		board.putPiece(player.getPiece(PieceName.ROOK2), row, col2);
	}
	private void placeBishop(int row, ChessBoard board, ChessPlayer player) {
		int col1 = 2, col2 = 5;
		board.putPiece(player.getPiece(PieceName.BISHOP1), row, col1);

		board.putPiece(player.getPiece(PieceName.BISHOP2), row, col2);
	}
	private void placeKing(int row, ChessBoard board, ChessPlayer player) {
		int col = 4;
		board.putPiece(player.getPiece(PieceName.KING), row, col);

	}
	private void placeQueen(int row, ChessBoard board, ChessPlayer player) {
		int col = 3;
		board.putPiece(player.getPiece(PieceName.QUEEN), row, col);

	}

	@Override
	public boolean isOver() {
		// TODO Auto-generated method stub
		return player1.getPiece(PieceName.KING).isDead() || player2.getPiece(PieceName.KING).isDead();
	}
	

}
