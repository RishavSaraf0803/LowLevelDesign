package FoodDeliverySystem.Data;

import java.util.List;

public class Menu {
	
	private final List<FoodItem> foodItems;

	public Menu(List<FoodItem> foodItems) {
		super();
		this.foodItems = foodItems;
	}

	public List<FoodItem> getFoodItems() {
		return foodItems;
	}
	
	
	

}
