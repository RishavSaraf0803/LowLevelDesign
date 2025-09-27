package stocktrading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockUpdatesSubscriber implements StockSubscriber {

	private final String name;
	private Map<StockName, StockValue> stockValues;
	
	private List<StockPublisher> stockPublishers;
	
	public StockUpdatesSubscriber(String name) {
		super();
		this.name = name;
		this.stockValues = new HashMap<>();
		this.stockPublishers = new ArrayList<>();
	}

	@Override
	public void update(StockName stockName, StockValue stockValue) {
		// TODO Auto-generated method stub
		System.out.println(name + " " + stockName + " " + stockValue.getAmount() + stockValue.getCurrency() + stockValue.getVersionName());
		if(!stockValues.containsKey(stockValue)) {
			stockValues.put(stockName, stockValue);
		}
		else if(stockValues.get(stockName).getVersionName() < stockValue.getVersionName()) {
			stockValues.put(stockName, stockValue);
		}
		else {
			throw new RuntimeException("Stock Version is past");
		}
		
	}

}
