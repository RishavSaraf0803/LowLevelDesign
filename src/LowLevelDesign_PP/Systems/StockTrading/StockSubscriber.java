package stocktrading;

public interface StockSubscriber {

	void update(StockName stockName, StockValue stockValue);
}
