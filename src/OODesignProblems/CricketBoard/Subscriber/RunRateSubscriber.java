package CricketBoard.Subscriber;

import CricketBoard.Publisher.CricketPublisher;

public class RunRateSubscriber implements CricketSubscriber {
	
	private int runs;
	private int wickets;
	private  float overs;
	
	private final CricketPublisher publisher;
	

	public RunRateSubscriber(CricketPublisher publisher) {
		super();
		this.publisher = publisher;
	}


	@Override
	public void update(CricketPublisher publisher) {
		// TODO Auto-generated method stub
		this.runs = publisher.getRuns();
		this.wickets = publisher.getWickets();
		this.overs = publisher.getOvers();

	}


	public int getRuns() {
		return runs;
	}


	public int getWickets() {
		return wickets;
	}


	public float getOvers() {
		return overs;
	}


	public CricketPublisher getPublisher() {
		return publisher;
	}


	
	
	

}
