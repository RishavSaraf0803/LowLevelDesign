package Chess;

public class King implements ChessPiece{

	private final PieceName pieceName;
	private final Color color;
	private  boolean isDead;

	

	public King(PieceName pieceName, Color color, boolean isDead) {
		super();
		this.pieceName = pieceName;
		this.color = color;
		this.isDead = isDead;
	}

	@Override
	public void move(ChessCell source, ChessCell destination, ChessBoard chessBoard) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isDead() {
		// TODO Auto-generated method stub
		return this.isDead;
	}

	@Override
	public void setDead(boolean isDead) {
		// TODO Auto-generated method stub
		this.isDead = isDead;
	}

	@Override
	public PieceName getName() {
		// TODO Auto-generated method stub
		return this.pieceName;
	}

	public Color getColor() {
		return color;
	}
	
	

}
