package Chess;

public class Move {
	
	private final Pair source;
	private final Pair destination;
	
	
	public Move(Pair source, Pair destination) {
		super();
		this.source = source;
		this.destination = destination;
	}
	public Pair getSource() {
		return source;
	}
	public Pair getDestination() {
		return destination;
	}
	

}
