package CricketBoard.Publisher;

import CricketBoard.Subscriber.FootBallSubscriber;
//import CricketBoard.Subscriber.Subscriber;

public interface FootBallPublisher {

	

	void subscribe(FootBallSubscriber subscriber);
	void unsubscribe(FootBallSubscriber subscriber);
	void notifyAll(int goals1, int goals2, float duration);
	

    public int getGoals1();
    public int getGoals2();
    public float getDuration();
}
