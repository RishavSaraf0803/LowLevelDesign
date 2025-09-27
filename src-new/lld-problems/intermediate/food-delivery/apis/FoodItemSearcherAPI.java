package FoodDeliverySystem.apis;

import java.util.ArrayList;
import java.util.List;

import FoodDeliverySystem.Data.CuisineType;
import FoodDeliverySystem.Data.FoodItem;
import FoodDeliverySystem.Data.MealType;
import FoodDeliverySystem.Data.StarRating;
import FoodDeliverySystem.Filter.CusineTypeFilter;
import FoodDeliverySystem.Filter.FoodItemFilter;
import FoodDeliverySystem.Filter.MealTypeFilter;
import FoodDeliverySystem.Filter.StarRatingFilter;
import FoodDeliverySystem.searcher.FoodItemSearcher;

public class FoodItemSearcherAPI {

	
	public List<FoodItem> searchFoodItems(String foodItemName, MealType mealType, List<CuisineType> cuisineType, StarRating starRating){
		
		List<FoodItemFilter> foodItemFilters = new ArrayList<>();
		if(mealType != null)
			foodItemFilters.add(new MealTypeFilter(mealType));
		if(cuisineType != null)
			foodItemFilters.add(new CusineTypeFilter(cuisineType));
		if(starRating != null)
			foodItemFilters.add(new StarRatingFilter(starRating));
		
		FoodItemSearcher foodItemSearcher = new FoodItemSearcher();
		List<FoodItem> foodItems = foodItemSearcher.search(foodItemName, foodItemFilters);
		
		return foodItems;
		
	}
}

// Searcher : Generic
// Get only those items whose (name matches foodItemName) AND (mT is mealType) AND (cT in cuisines) AND (r > rating )
//Filter F1 F2 F3
//List of foodItems