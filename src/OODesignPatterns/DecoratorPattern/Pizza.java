package DecoratorPattern;

public abstract class Pizza {

	private String name;
	private double cost;
	public Pizza(String name, double cost) {
		super();
		this.name = name;
		this.cost = cost;
	}
	public String getName() {
		return name;
	}
	public double getCost() {
		return cost;
	}
	
}
