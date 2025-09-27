package FoodDeliverySystem.permission;

import FoodDeliverySystem.Data.FoodItem;
import FoodDeliverySystem.Data.Restaurant;
import FoodDeliverySystem.Data.User;
import FoodDeliverySystem.Managers.DeliveryManager;
import FoodDeliverySystem.searcher.RestaurantSearcher;

public class AddToCartPermission implements Permission {
	private final User user;
	private final FoodItem foodItem;
	private final DeliveryManager deliveryManager;
	
	
	public AddToCartPermission(User user, FoodItem foodItem) {
		super();
		this.user = user;
		this.foodItem = foodItem;
		this.deliveryManager = new DeliveryManager();
	}


	@Override
	public boolean isPermitted() {
		// TODO Auto-generated method stub
		
		if(!foodItem.isAvailable()) {
			return false;
		}
		Restaurant restaurant = new RestaurantSearcher().searchById(foodItem.getRestaurantId());
		return deliveryManager.isDeliveryPossible(restaurant.getAddress(), user.getUserAddress());
		
	}

}
