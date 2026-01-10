package DecoratorPattern;

public class Onion extends Topping {

	public Onion(Pizza pizza){
		super("onion", 50.0, pizza);
	}
}
