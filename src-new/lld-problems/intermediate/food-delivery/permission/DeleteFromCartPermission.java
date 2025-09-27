package FoodDeliverySystem.permission;

import FoodDeliverySystem.Data.FoodItem;
import FoodDeliverySystem.Data.User;

public class DeleteFromCartPermission implements Permission {

	private final User user;
	private final FoodItem foodItem;
	
	
	public DeleteFromCartPermission(User user, FoodItem foodItem) {
		super();
		this.user = user;
		this.foodItem = foodItem;
	}


	@Override
	public boolean isPermitted() {
		// TODO Auto-generated method stub
		return false;
	}

}
