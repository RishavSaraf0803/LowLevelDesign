package FoodDeliverySystem.apis;

import java.util.ArrayList;
import java.util.List;

import FoodDeliverySystem.Data.CuisineType;
import FoodDeliverySystem.Data.MealType;
import FoodDeliverySystem.Data.Restaurant;
import FoodDeliverySystem.Data.StarRating;
import FoodDeliverySystem.Filter.CusineTypeFilter;
import FoodDeliverySystem.Filter.MealTypeFilter;
import FoodDeliverySystem.Filter.RestaurantFilter;
import FoodDeliverySystem.Filter.StarRatingFilter;
import FoodDeliverySystem.searcher.RestaurantSearcher;

public class RestaurantSearcherAPI {

	
	public List<Restaurant> searchRestaurant(String restaurantName, MealType mealType, List<CuisineType> cusineType, StarRating starRating){
		//validation
		
		List<RestaurantFilter> restaurantFilter = new ArrayList<>();
		if(mealType != null)
			restaurantFilter.add(new MealTypeFilter(mealType));
		
		if(cusineType != null) {
			restaurantFilter.add(new CusineTypeFilter(cusineType));
		}
		if(starRating != null) {
			restaurantFilter.add(new StarRatingFilter(starRating));
		}
		RestaurantSearcher restaurantSearcher = new RestaurantSearcher();
		List<Restaurant> restaurants = restaurantSearcher.search(restaurantName, restaurantFilter);
		return restaurants;
	}
}
