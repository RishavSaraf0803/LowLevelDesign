package stocktrading;

public interface StockPublisher {

	void subscribe(StockSubscriber subscriber);
	void unsubscribe(StockSubscriber subscriber);
	void notifyAll(StockName stockName, StockValue stockValue);
}
