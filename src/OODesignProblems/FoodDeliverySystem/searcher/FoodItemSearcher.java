package FoodDeliverySystem.searcher;
import java.util.ArrayList;
import java.util.List;

import FoodDeliverySystem.Data.*;
import FoodDeliverySystem.DataAccessor.DataAccessObjectConverter;
import FoodDeliverySystem.DataAccessor.DataAccessResult;
import FoodDeliverySystem.DataAccessor.DataAccessor;
import FoodDeliverySystem.Filter.FoodItemFilter;
public class FoodItemSearcher {

	public List<FoodItem> search(String foodItemName, List<FoodItemFilter> filters){
		
		if(foodItemName == null || foodItemName.length() == 0 || filters == null) {
			throw new IllegalArgumentException("Missing Params");
		}
		
		DataAccessResult dataAccessResult = DataAccessor.getFoodItemWithName(foodItemName);
		
		List<FoodItem> foodItems = DataAccessObjectConverter.convertToFoodItems(dataAccessResult);
		
		List<FoodItem> filtedFoodItems = new ArrayList<>();
		
		for(FoodItem foodItem: foodItems) {
			boolean filter = true;
			for(FoodItemFilter foodItemFilter: filters) {
				filter = filter & foodItemFilter.filter(foodItem);
			}
			
			if(filter == true) {
				filtedFoodItems.add(foodItem);
			}
		}
		
		
		return filtedFoodItems;
		
	}
	
	public FoodItem searchById(int foodItemId) throws Exception {
		
		DataAccessResult dataAccessResult = DataAccessor.getFoodItemWithId(foodItemId);
		
		List<FoodItem> foodItems = DataAccessObjectConverter.convertToFoodItems(dataAccessResult);
		
		if(foodItems == null) {
			throw new Exception("No FoodItem present with id");
		}
		
		return foodItems.get(0);

	}
}
