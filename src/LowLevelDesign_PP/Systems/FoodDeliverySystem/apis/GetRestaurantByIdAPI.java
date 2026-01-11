package FoodDeliverySystem.apis;

import FoodDeliverySystem.Data.Restaurant;
import FoodDeliverySystem.searcher.RestaurantSearcher;

public class GetRestaurantByIdAPI {

	public Restaurant getRestaurantById(int restaurantId) {
		
		if(restaurantId <= 0) {
			throw new IllegalArgumentException("Restaurant id can not be negative");
		}
		Restaurant restaurant = new RestaurantSearcher().searchById(restaurantId);
		return restaurant;
	}
}
