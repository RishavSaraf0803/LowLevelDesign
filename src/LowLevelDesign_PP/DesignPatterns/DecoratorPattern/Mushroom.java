package DecoratorPattern;

public class Mushroom extends Topping {

	public Mushroom(Pizza pizza) {
		super("Mushroom", 100.0, pizza);
	}
}
