package Chess;

import java.util.Optional;

public class ChessCell {
	
	private final int x;

	private final int y;
	private Optional<ChessPiece> chessPiece;
	public ChessCell(int x, int y) {
		super();
		this.x = x;
		this.y = y;
		this.chessPiece = Optional.empty();
	}
	public Optional<ChessPiece> getChessPiece() {
		return chessPiece;
	}
	public void setChessPiece(Optional<ChessPiece> chessPiece) {
		this.chessPiece = Optional.ofNullable(chessPiece.get());
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
	
	
	

}
