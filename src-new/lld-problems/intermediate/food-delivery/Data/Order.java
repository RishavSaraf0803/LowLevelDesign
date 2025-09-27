package FoodDeliverySystem.Data;

import java.util.List;

public class Order {

	private final int orderId;
	private final int userId;
	private final OrderStatus orderStatus;
	private final List<CartItem> cartItems;
	public Order(int orderId, int userId, OrderStatus orderStatus, List<CartItem> cartItems) {
		super();
		this.orderId = orderId;
		this.userId = userId;
		this.orderStatus = orderStatus;
		this.cartItems = cartItems;
	}
	public int getOrderId() {
		return orderId;
	}
	public int getUserId() {
		return userId;
	}
	public OrderStatus getOrderStatus() {
		return orderStatus;
	}
	public List<CartItem> getCartItems() {
		return cartItems;
	}
	
	
	
}
