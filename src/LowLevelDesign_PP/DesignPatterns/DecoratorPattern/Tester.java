package DecoratorPattern;

public class Tester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Pizza pizza = new Mushroom(new Olive(new Onion(new WheatBase())));
		System.out.println(pizza.getName() + " "+ pizza.getCost());
		
	}

}
