package Chess;

import java.util.Map;

public class ChessPlayer extends Player {
 
	private final Map<PieceName, ChessPiece> pieces;
	
	private final ChessBoard chessBoard;

	
	
	public ChessPlayer(String name, Map<PieceName, ChessPiece> pieces, ChessBoard chessBoard) {
		super(name);
		this.pieces = pieces;
		this.chessBoard = chessBoard; 
	}




	public ChessPiece getPiece(PieceName pieceName) {
		if(!pieces.containsKey(pieceName)) {
			throw new IllegalArgumentException("invalid arg");
		}
		return this.pieces.get(pieceName);
	}




	public Map<PieceName, ChessPiece> getPieces() {
		return pieces;
	}




	public ChessBoard getChessBoard() {
		return chessBoard;
	}




	@Override
	public Move makeMove() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
