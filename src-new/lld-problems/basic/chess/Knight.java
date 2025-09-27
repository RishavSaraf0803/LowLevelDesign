package Chess;

public class Knight implements ChessPiece {

	private final PieceName pieceName;
	private final Color color;
	private  boolean isDead;

	

	public Knight(PieceName pieceName, Color color, boolean isDead) {
		super();
		this.pieceName = pieceName;
		this.color = color;
		this.isDead = isDead;
	}

	@Override
	public void move(ChessCell source, ChessCell destination, ChessBoard chessBoard) {
		// TODO Auto-generated method stub
		
		int x_diff = Math.abs(source.getX() - destination.getX());
		int y_diff = Math.abs(source.getX() - destination.getY());
		
		//Legal move
		if(!(Math.max(x_diff, y_diff) == 2 && Math.min(x_diff, y_diff) == 1)) {
			throw new RuntimeException("Invalid Move");
		}
		
		if(destination.getChessPiece().isPresent()) {
			if(destination.getChessPiece().get().getColor().equals(this.color)) {
				throw new RuntimeException("Destination invalid, Contains Piece");
			}
			else {
				destination.getChessPiece().get().setDead(true);
			}
		}
		chessBoard.removePiece(source.getX(), source.getY());
		chessBoard.putPiece(this, destination.getX(), destination.getY());
		
	}
	
	

	public Color getColor() {
		return color;
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

}
