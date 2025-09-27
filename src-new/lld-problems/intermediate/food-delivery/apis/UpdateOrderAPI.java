package FoodDeliverySystem.apis;

import FoodDeliverySystem.Data.Order;
import FoodDeliverySystem.Data.OrderStatus;
import FoodDeliverySystem.Data.User;
import FoodDeliverySystem.Managers.OrderManager;
import FoodDeliverySystem.Managers.UserManager;

public class UpdateOrderAPI {
	
	UserManager userManager = new UserManager();
	OrderManager orderManager = new OrderManager();
	
	public void updateOrder(int orderId, OrderStatus orderStatus,String userToken) {
		//..
		User user = userManager.getUserByToken(userToken);
		//..
		Order order = orderManager.getOrder(orderId);
		//..
		if(orderStatus.equals(OrderStatus.COOKING)) {
			orderManager.setOrderToReadyForDelivery(user, order);
		}
	}
}
