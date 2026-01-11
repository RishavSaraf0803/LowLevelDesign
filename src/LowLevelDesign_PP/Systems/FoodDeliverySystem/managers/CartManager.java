package FoodDeliverySystem.Managers;

import java.util.List;

import FoodDeliverySystem.Data.CartItem;
import FoodDeliverySystem.Data.FoodItem;
import FoodDeliverySystem.Data.User;
import FoodDeliverySystem.DataAccessor.DataAccessObjectConverter;
import FoodDeliverySystem.DataAccessor.DataAccessResult;
import FoodDeliverySystem.DataAccessor.DataAccessor;
import FoodDeliverySystem.Factory.PermissionFactory;
import FoodDeliverySystem.permission.Permission;

public class CartManager {
	
	public List<CartItem> getUserCart(User user){
//		UserManager userManager = new UserManager();
//		if(userManager.getUserById(user.getUserId()).getUserId() != user.getUserId()) {
//			throw new RuntimeException("User id does not match");
//		}
		
		DataAccessResult dataAccessResult = DataAccessor.getCartForUser(user);
		
		List<CartItem> cartItems = DataAccessObjectConverter.convertToCartItems(dataAccessResult);
		
		if(cartItems.isEmpty()) {
			throw new RuntimeException("Cart is Empty");
		}
		return cartItems;
		
		
	}
	//adds 1 unit 
	public void addItemToCart(User user, FoodItem foodItem) {
		Permission permission = PermissionFactory.getAddToCartPermission(user, foodItem);
		//Permission permission = new AddToCartPermission(user, foodItem);
		if(!permission.isPermitted()) {
			throw new RuntimeException("Permission Denied");
		}
		if(!isFoodItemFromSameRestaurant(user, foodItem)) {

			throw new RuntimeException("Your Cart contains items from diff restaurant");
		}
		
		DataAccessor.addItemToCart(user,foodItem);
		
	}
	// remove 1 unit 
	public void deleteItemFromCart(User user, FoodItem foodItem) {
		Permission permission = PermissionFactory.getDeleteFromCartPermission(user, foodItem);
		if(!permission.isPermitted()) {
			throw new RuntimeException("Permission Denied");
		}
		if(!isFoodItemPresentInCart(user,foodItem)) {
			throw new RuntimeException("Cart does not contain food Item");
		}
		DataAccessor.deleteItemFromCart(user, foodItem);
		
	}
	public void checkOutUsersCart(User user) {
		Permission permission = PermissionFactory.getCheckoutCartPermission(user);
		
		if(!permission.isPermitted()) {
			throw new RuntimeException("Permission Denied");
		}
		if(isCartEmpty(user)) {
			throw new RuntimeException("Cart is Empty");
		}
		DataAccessor.checkOutUsersCart(user);
		
		
	}
	private boolean isCartEmpty(User user) {
		return getUserCart(user).isEmpty();
	}
	private boolean isFoodItemPresentInCart(User user, FoodItem foodItem) {
		List<CartItem> cartItems = getUserCart(user);
		
		for(CartItem cartItem: cartItems) {
			if(cartItem.getFoodItem().getId() == foodItem.getId()) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isFoodItemFromSameRestaurant(User user, FoodItem foodItem) {
		List<CartItem> cartItems = getUserCart(user);
		if(cartItems.isEmpty() || cartItems.get(0).getFoodItem().getRestaurantId() == foodItem.getRestaurantId()) {
			return true;
		}
//		int restaurantId = foodItem.getRestaurantId();
//		for(CartItem cartItem: cartItems) {
//			if(cartItem.getFoodItem().getRestaurantId() != restaurantId) {
//				return false;
//			}
//		}
		return false;
	}
	
}
// Cart
//cart_id, user_id,food_item_idquantity,status
//permission
//Systems: diff level of access
//Read Write Execute
//Permission
//AWS
/*
Orders
order_id,cart_id
*/