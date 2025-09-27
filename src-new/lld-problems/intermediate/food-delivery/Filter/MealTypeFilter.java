package FoodDeliverySystem.Filter;

import FoodDeliverySystem.Data.FoodItem;
import FoodDeliverySystem.Data.MealType;
import FoodDeliverySystem.Data.Restaurant;

public class MealTypeFilter implements FoodItemFilter, RestaurantFilter {

	private final MealType mealType;
	
	
	public MealTypeFilter(MealType mealType) {
		super();
		this.mealType = mealType;
	}


	@Override
	public boolean filter(FoodItem foodItem) {
		// TODO Auto-generated method stub
		return foodItem.getMealType().equals(mealType);
	}

@Override
	public boolean filter(Restaurant restaurant) {
		// TODO Auto-generated method stub
		return restaurant.getMealType().equals(mealType);
	}

}
