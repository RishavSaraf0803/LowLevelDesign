package FoodDeliverySystem.Factory;

import FoodDeliverySystem.Data.FoodItem;
import FoodDeliverySystem.Data.Order;
import FoodDeliverySystem.Data.OrderStatus;
import FoodDeliverySystem.Data.User;
import FoodDeliverySystem.permission.AddToCartPermission;
import FoodDeliverySystem.permission.CheckoutCartPermission;
import FoodDeliverySystem.permission.DeleteFromCartPermission;
import FoodDeliverySystem.permission.Permission;
import FoodDeliverySystem.permission.PlaceOrderPermission;
import FoodDeliverySystem.permission.UpdateOrderPermission;

public class PermissionFactory {
	
	private PermissionFactory() {
		
	}

	public static Permission getAddToCartPermission(User user, FoodItem foodItem){
		
		return new AddToCartPermission(user,foodItem);
	}
	public static Permission getDeleteFromCartPermission(User user, FoodItem foodItem) {
		return new DeleteFromCartPermission(user,foodItem);
	}
	
	public static Permission getCheckoutCartPermission(User user) {
		return new CheckoutCartPermission(user);
	}
	
	public static Permission getPlaceOrderPermission(User user) {
		return new PlaceOrderPermission(user);
	}
	
	public static Permission updateOrderPermission(User user, Order order, OrderStatus orderStatus) {
		return new UpdateOrderPermission(user, order, orderStatus);
	}
}
