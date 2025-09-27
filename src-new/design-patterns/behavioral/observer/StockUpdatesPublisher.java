package stocktrading;

import java.util.ArrayList;
import java.util.List;

public class StockUpdatesPublisher implements StockPublisher {

	private final String name;
	private List<StockSubscriber> subscribers;
	
	
	public StockUpdatesPublisher(String name) {
		super();
		this.name = name;
		this.subscribers = new ArrayList<>();
	}

	@Override
	public void subscribe(StockSubscriber subscriber) {
		// TODO Auto-generated method stub
		subscribers.add(subscriber);
	}

	@Override
	public void unsubscribe(StockSubscriber subscriber) {
		// TODO Auto-generated method stub
		subscribers.remove(subscriber);
	}

	@Override
	public void notifyAll(StockName stockName, StockValue stockValue) {
		// TODO Auto-generated method stub
		for(StockSubscriber subscriber: subscribers) {
			subscriber.update(subscriber);
		}
	}

}
