package FoodDeliverySystem.DataAccessor;

import java.util.List;

import FoodDeliverySystem.Data.CartItem;
import FoodDeliverySystem.Data.FoodItem;
import FoodDeliverySystem.Data.Order;
import FoodDeliverySystem.Data.OrderStatus;
import FoodDeliverySystem.Data.User;

public class DataAccessor {

	public static void addItemToCart(User user, FoodItem foodItem) {
		
	}
	
	public static DataAccessResult getFoodItemWithName(String foodItemName) {
		return null;
		
	}

	public static DataAccessResult getFoodItemWithId(int foodItemId) {
		// TODO Auto-generated method stub
		return null;
	}

	public static DataAccessResult getRestaurantWithName(String restaurantName) {
		// TODO Auto-generated method stub
		return null;
	}

	public static DataAccessResult getRestaurantWithId(int restaurantId) {
		// TODO Auto-generated method stub
		return null;
	}

	public static DataAccessResult getCartForUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void deleteItemFromCart(User user, FoodItem foodItem) {
		// TODO Auto-generated method stub
		
	}

	public DataAccessResult getUserById(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void checkOutUsersCart(User user) {
		// TODO Auto-generated method stub
		
	}
	public static int createOrder(User user, List<CartItem> cartItems) {
		return 1;
	}
	
	public static void changeOrderStatus(User user, Order order, OrderStatus orderStatus) {
		
	}

	
	
	
}
