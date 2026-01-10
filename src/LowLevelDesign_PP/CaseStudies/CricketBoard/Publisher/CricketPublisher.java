package CricketBoard.Publisher;

import CricketBoard.Subscriber.CricketSubscriber;

public interface CricketPublisher {
	
	void subscribe(CricketSubscriber subscriber);
	void unsubscribe(CricketSubscriber subscriber);
	void notifyAll(int runs, int wickets, float overs);

	public int getRuns();
	


	public int getWickets();


	public float getOvers();

}
