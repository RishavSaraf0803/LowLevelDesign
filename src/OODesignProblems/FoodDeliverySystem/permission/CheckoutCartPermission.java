package FoodDeliverySystem.permission;

import FoodDeliverySystem.Data.User;

public class CheckoutCartPermission implements Permission {

	private final User user;
	
	
	public CheckoutCartPermission(User user) {
		super();
		this.user = user;
	}


	@Override
	public boolean isPermitted() {
		// TODO Auto-generated method stub
		return false;
	}

}
