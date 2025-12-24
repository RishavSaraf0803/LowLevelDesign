package logframework;

import java.util.ArrayList;
import java.util.List;

public class LogPublisher implements Publisher{

	
	private final List<Subscriber> subscribers;

	public LogPublisher() {
	subscribers = new ArrayList<>();
	}

	@Override
	public void subscribe(Subscriber subscriber) {
		// TODO Auto-generated method stub
		subscribers.add(subscriber);
	}

	@Override
	public void unsubscribe(Subscriber subscriber) {
		// TODO Auto-generated method stub
		subscribers.remove(subscriber);
	}

	@Override
	public void notify(String message) {
		// TODO Auto-generated method stub
		for(Subscriber subscriber : subscribers) {
			subscriber.update(message);
		}
	}
	
	
}
