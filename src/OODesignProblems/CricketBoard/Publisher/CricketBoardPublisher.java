package CricketBoard.Publisher;

import java.util.List;

import CricketBoard.Subscriber.CricketSubscriber;
//import CricketBoard.Subscriber.Subscriber;

public class CricketBoardPublisher implements CricketPublisher {
	
	private  int runs;
	private  int wickets;
	private  float overs;
	
	private List<CricketSubscriber> subscribers;
	
	
	
	
	

	public CricketBoardPublisher(int runs, int wickets, float overs, List<CricketSubscriber> subscribers) {
		super();
		this.runs = runs;
		this.wickets = wickets;
		this.overs = overs;
		this.subscribers = subscribers;
	}






	@Override
	public void notifyAll(int runs, int wickets, float overs) {
		// TODO Auto-generated method stub
		this.runs = runs;
		this.wickets = wickets;
		this.overs = overs;
		for(CricketSubscriber subscriber: subscribers) {
			subscriber.update(this);
		}

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






	public List<CricketSubscriber> getSubscribers() {
		return subscribers;
	}






	@Override
	public void subscribe(CricketSubscriber subscriber) {
		// TODO Auto-generated method stub
		
	}






	@Override
	public void unsubscribe(CricketSubscriber subscriber) {
		// TODO Auto-generated method stub
		
	}













	

}
