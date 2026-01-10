package CricketBoard.Publisher;

import java.util.List;

import CricketBoard.Subscriber.FootBallSubscriber;
//import CricketBoard.Subscriber.Subscriber;

public class FootBallScorePublisher implements FootBallPublisher {
	
	int goals1;
	int goals2;
	float duration;
	List<FootBallSubscriber> subscribers;
	

	public FootBallScorePublisher(int goals1, int goals2, float duration, List<FootBallSubscriber> subscribers) {
		super();
		this.goals1 = goals1;
		this.goals2 = goals2;
		this.duration = duration;
		this.subscribers = subscribers;
	}

	public int getGoals1() {
		return goals1;
	}

	public int getGoals2() {
		return goals2;
	}

	public float getDuration() {
		return duration;
	}

	public List<FootBallSubscriber> getSubscribers() {
		return subscribers;
	}

	@Override
	public void subscribe(FootBallSubscriber subscriber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unsubscribe(FootBallSubscriber subscriber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyAll(int goals1, int goals2, float duration) {
		// TODO Auto-generated method stub
		
	}






}
