package stocktrading;

public class StockValue {

	private final int versionName;
	private final double amount;
	private final Currency currency;
	public StockValue(int versionName, double amount, Currency currency) {
		super();
		this.versionName = versionName;
		this.amount = amount;
		this.currency = currency;
	}
	public int getVersionName() {
		return versionName;
	}
	public double getAmount() {
		return amount;
	}
	public Currency getCurrency() {
		return currency;
	}
	
}
