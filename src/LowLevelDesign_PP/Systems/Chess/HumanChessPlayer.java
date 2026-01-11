package Chess;

import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class HumanChessPlayer extends ChessPlayer {

	public HumanChessPlayer(String name, Map<PieceName, ChessPiece> pieces, ChessBoard chessBoard) {
		super(name, pieces, chessBoard);
		// TODO Auto-generated constructor stub
	}

	public Move makeMove() {
		getChessBoard().display();
		int x, y;
		Scanner sc = new Scanner(System.in);
		x = sc.nextInt();
		y = sc.nextInt();
		
		Optional<ChessPiece> chessPiece = getChessBoard().getCell(new Pair(x,y)).getChessPiece(); 
		
		if(!chessPiece.isPresent()) {
			throw new RuntimeException("Invalid spot");
		}
		
		if(chessPiece.get().equals( getPiece(chessPiece.get().getName()))) {
			throw new RuntimeException("Invalid piece"); 
		}
		
		Pair source = new Pair(x,y);
		x = sc.nextInt(); y = sc.nextInt();
		Pair destination  = new Pair(x,y);
		
		return new Move(source, destination);
	}

}
