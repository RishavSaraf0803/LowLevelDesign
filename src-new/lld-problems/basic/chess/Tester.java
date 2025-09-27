package Chess;

import java.util.HashMap;
import java.util.Map;

public class Tester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ChessBoard chessBoard = new ChessBoard();
		ChessPlayer firstPlayer = new HumanChessPlayer("Vivek", getPieces(Color.WHITE), chessBoard);

		ChessPlayer secondPlayer = new HumanChessPlayer("Rishav",getPieces(Color.BLACK), chessBoard);
		
		BoardGame chessBoardGame = new Chess(chessBoard,  firstPlayer, secondPlayer);
		
		chessBoardGame.startGame();

	}
	private static Map<PieceName, ChessPiece> getPieces(Color color){
		
		Map<PieceName, ChessPiece> map = new HashMap<>();
		map.put(PieceName.BISHOP1, new Bishop(PieceName.BISHOP1,color, false));

		map.put(PieceName.BISHOP2, new Bishop(PieceName.BISHOP2,color, false));
		
		map.put(PieceName.KNIGHT1, new Knight(PieceName.KNIGHT1,color, false));

		map.put(PieceName.KNIGHT2, new Knight(PieceName.KNIGHT2,color, false));
		
		map.put(PieceName.ROOK1, new Rook(PieceName.ROOK1,color, false));

		map.put(PieceName.ROOK2, new Rook(PieceName.ROOK2,color, false));
		
		map.put(PieceName.KING, new King(PieceName.KING, color, false));

		map.put(PieceName.QUEEN, new Queen(PieceName.QUEEN ,color, false));

		map.put(PieceName.PAWN1, new Pawn(PieceName.PAWN1,color, false));
		map.put(PieceName.PAWN2, new Pawn(PieceName.PAWN2,color, false));
		map.put(PieceName.PAWN3, new Pawn(PieceName.PAWN3,color, false));
		map.put(PieceName.PAWN4, new Pawn(PieceName.PAWN4,color, false));
		map.put(PieceName.PAWN5, new Pawn(PieceName.PAWN5,color, false));
		map.put(PieceName.PAWN6, new Pawn(PieceName.PAWN6,color, false));
		map.put(PieceName.PAWN7, new Pawn(PieceName.PAWN7,color, false));
		map.put(PieceName.PAWN8, new Pawn(PieceName.PAWN8,color, false));
		return map; 

		
		
	}

}
