package FoodDeliverySystem.searcher;

import java.util.ArrayList;
import java.util.List;

import FoodDeliverySystem.Data.FoodItem;
import FoodDeliverySystem.Data.Restaurant;
import FoodDeliverySystem.DataAccessor.DataAccessObjectConverter;
import FoodDeliverySystem.DataAccessor.DataAccessResult;
import FoodDeliverySystem.DataAccessor.DataAccessor;
import FoodDeliverySystem.Filter.FoodItemFilter;
import FoodDeliverySystem.Filter.RestaurantFilter;

public class RestaurantSearcher {

	public List<Restaurant> search(String restaurantName, List<RestaurantFilter> filters){
		if(restaurantName == null || restaurantName.length() == 0 || filters == null) {
			throw new IllegalArgumentException("Invalid Params");
		}
		DataAccessResult dataAccessResult = DataAccessor.getRestaurantWithName(restaurantName);
		
		List<Restaurant> restaurants = DataAccessObjectConverter.convertToRestaurant(dataAccessResult);
		
		List<Restaurant> filtedFoodItems = new ArrayList<>();
		
		for(RestaurantFilter restaurantFilter : filters) {
			List<Restaurant> filtedRestaurant = new ArrayList<>();
			for(Restaurant restaurant : restaurants ) {
				if(restaurantFilter.filter(restaurant)) {
					filtedRestaurant.add(restaurant);
				}
			}
			restaurants = filtedRestaurant;
		}
		return restaurants;
	}
	
	public Restaurant searchById(int restaurantId) {
		DataAccessResult dataAccessResult = DataAccessor.getRestaurantWithId(restaurantId);
		
		List<Restaurant> restaurants = DataAccessObjectConverter.convertToRestaurant(dataAccessResult);
		
		if(restaurants != null) {
			return restaurants.get(0);
		}
		return null;
	}
}
