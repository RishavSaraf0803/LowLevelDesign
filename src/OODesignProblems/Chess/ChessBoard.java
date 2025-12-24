package Chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChessBoard implements Board {
	
	private List<List<ChessCell>> cells;
	
	
	private static final int rowCount = 8;

	private static final int colCount = 8;
	

	public ChessBoard() {
		
		this.cells = new ArrayList<>();
		for(int i = 0; i < rowCount; i++) {
			List<ChessCell> chessCells = new ArrayList<>();
			for(int j = 0; j  < colCount; j++) {
				chessCells.add(new ChessCell(i,j));
			}
			this.cells.add(chessCells);
		}
		//this.display();
	}

	@Override
	public void display() {
		// TODO Auto-generated method stub
		
		for(int row = 0; row < rowCount; row++) {
			for(int col = 0; col < colCount; col++) {
				 Pair  pair = new Pair(row, col);
				 Optional<ChessPiece> chessPiece = getCell(pair).getChessPiece();
				 if(!chessPiece.isPresent()) {
					 System.out.print("0 | ");
				 }
				 else {
					 System.out.print(chessPiece.get().getName() + " ");
				 } 
			}
			System.out.println(); 
		}
		  
	}

	@Override
	public void applyMove(Move move) {
		// TODO Auto-generated method stub
		ChessCell sourceCell = getCell(move.getSource());
		ChessPiece chessPiece = sourceCell.getChessPiece().get();
		ChessCell destinationCell = getCell(move.getDestination());
		chessPiece.move(sourceCell, destinationCell, this );
		
		
	}
	
	public ChessCell getCell(Pair pair) {
		return new ChessCell(pair.getX(), pair.getY());
	}
	public void putPiece(ChessPiece chessPiece, int row, int col) {
		 ChessCell chessCell = new ChessCell(row, col);
		chessCell.setChessPiece(Optional.of(chessPiece));
		this.cells.get(row).add(col, chessCell);
	}
	public void removePiece(int row, int col) {
		cells.get(row).get(col).setChessPiece(Optional.empty());
	}
	

}
