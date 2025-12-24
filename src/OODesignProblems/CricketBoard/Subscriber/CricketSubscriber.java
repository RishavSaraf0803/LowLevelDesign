package CricketBoard.Subscriber;

import CricketBoard.Publisher.CricketPublisher;
import CricketBoard.Publisher.FootBallPublisher;
//import CricketBoard.Publisher.Publisher;

public interface CricketSubscriber {

	void update(CricketPublisher publisher);
}
