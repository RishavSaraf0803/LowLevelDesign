package FoodDeliverySystem.apis;

import java.util.List;

import FoodDeliverySystem.Data.FoodItem;
import FoodDeliverySystem.Data.User;
import FoodDeliverySystem.Managers.CartManager;
import FoodDeliverySystem.Managers.UserManager;
import FoodDeliverySystem.searcher.FoodItemSearcher;

public class AddToCartAPI {
	private final UserManager userManager = new UserManager();
	private final FoodItemSearcher foodItemSearcher = new FoodItemSearcher();
	private final CartManager cartManager = new CartManager();
	
	public void addToCart(int foodItemId, String userToken) throws Exception {
		
		if(userToken == null || userToken.length() == 0 || foodItemId < 0) {
			throw new IllegalArgumentException("invalid params");
		}
		
		User user = userManager.getUserByToken(userToken);
		if(user == null) {
			throw new Exception("No User Fetch for given User Id;");
		}
		FoodItem foodItem = foodItemSearcher.searchById(foodItemId);
		
		if(foodItem == null) {
			throw new Exception("No Food Item Fetch for given FoodItem Id");
		}
		
		cartManager.addItemToCart(user, foodItem);
		
		return;
		
	}
	

}
