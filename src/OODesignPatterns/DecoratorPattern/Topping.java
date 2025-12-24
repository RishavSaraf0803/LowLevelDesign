package DecoratorPattern;

public class Topping extends Pizza {


	private final Pizza pizza;
	public Topping(String name, double cost, Pizza pizza) {
		super(name, cost);
		// TODO Auto-generated constructor stub
		this.pizza = pizza;
	}
	public Pizza getPizza() {
		return pizza;
	}

	
	public String getName() {
		return super.getName() + " " + pizza.getName();
	}
	public double getCost() {
		return super.getCost() + pizza.getCost();
	}
	
	
}
//Delegation of work