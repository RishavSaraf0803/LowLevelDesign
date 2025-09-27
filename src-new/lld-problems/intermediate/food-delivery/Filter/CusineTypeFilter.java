package FoodDeliverySystem.Filter;

import java.util.List;

import FoodDeliverySystem.Data.CuisineType;
import FoodDeliverySystem.Data.FoodItem;
import FoodDeliverySystem.Data.Restaurant;

public class CusineTypeFilter implements FoodItemFilter,RestaurantFilter {

	private final List<CuisineType> cuisineType;
	
	
	public CusineTypeFilter(List<CuisineType> cuisineType) {
		super();
		this.cuisineType = cuisineType;
	}


	@Override
	public boolean filter(FoodItem foodItem) {
		// TODO Auto-generated method stub
		return cuisineType.contains(foodItem.getCusineType());
	}


	@Override
	public boolean filter(Restaurant restaurant) {
		// TODO Auto-generated method stub
		List<CuisineType> restaurantCuisineTypes = restaurant.getCuisineType();
		for(CuisineType restaurantCuisineType : restaurantCuisineTypes) {
			if(cuisineType.contains(restaurantCuisineType)) {
				return true;
			}
		}
		return false ;
	}


	

}
