package FoodDeliverySystem.permission;

import FoodDeliverySystem.Data.Order;
import FoodDeliverySystem.Data.OrderStatus;
import FoodDeliverySystem.Data.User;

public class UpdateOrderPermission implements Permission {

	
	private final User user;
	private final Order order;
	private final OrderStatus orderStatus;
	
	
	public UpdateOrderPermission(User user, Order order, OrderStatus orderStatus) {
		super();
		this.user = user;
		this.order = order;
		this.orderStatus = orderStatus;
	}


	@Override
	public boolean isPermitted() {
		// TODO Auto-generated method stub
		return false;
	}

}
