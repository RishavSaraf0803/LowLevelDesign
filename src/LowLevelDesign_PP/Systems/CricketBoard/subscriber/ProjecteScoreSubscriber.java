package CricketBoard.Subscriber;

import java.util.List;

import CricketBoard.Publisher.CricketPublisher;


public class ProjecteScoreSubscriber implements CricketSubscriber {

	private int runs;
	private int wickets;
	private  float overs;
	
	private final List<CricketPublisher> publishers;
	
	
	public ProjecteScoreSubscriber(int runs, int wickets, float overs, List<CricketPublisher> publishers) {
		super();
		this.runs = runs;
		this.wickets = wickets;
		this.overs = overs;
		this.publishers = publishers;
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


	public List<CricketPublisher> getPublisher() {
		return publishers;
	}


	
	

}
