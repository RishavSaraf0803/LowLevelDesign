package FoodDeliverySystem.Managers;

import java.util.List;

import FoodDeliverySystem.Data.CartItem;
import FoodDeliverySystem.Data.Order;
import FoodDeliverySystem.Data.OrderStatus;
import FoodDeliverySystem.Data.User;
import FoodDeliverySystem.DataAccessor.DataAccessor;
import FoodDeliverySystem.Factory.PermissionFactory;
import FoodDeliverySystem.permission.Permission;

public class OrderManager {
	
	public Order placeOrder(User user) {
		Permission permission = PermissionFactory.getPlaceOrderPermission(user);
		if(!permission.isPermitted()) {
			throw new RuntimeException("Permission Denied");
		}
	  CartManager cartManager = new CartManager();
	  List<CartItem> cartItems = cartManager.getUserCart(user);
	  
	  int orderId = DataAccessor.createOrder(user, cartItems);
	  cartManager.checkOutUsersCart(user);
	  return new Order(orderId, user.getUserId(),OrderStatus.ORDER_PLACED, cartItems);
		
		
	}
	
	public Order getOrder(int orderId)
	{
		return null;
		
	}
	public List<Order> getOrder(User user){
		return null;
	}
	
	public void setOrderToCooking(User user, Order order) {
		Permission permission = PermissionFactory.updateOrderPermission(user, order, OrderStatus.COOKING);
		if(!permission.isPermitted()) {
			throw new RuntimeException("Permission Denied");
		}
		if(!order.getOrderStatus().equals(OrderStatus.ORDER_PLACED)) {
			throw new RuntimeException("Only order placed status to cooking");
		}
		DataAccessor.changeOrderStatus(user, order, OrderStatus.COOKING);
		
	}
	public void setOrderToReadyForDelivery(User user, Order order) {
		Permission permission = PermissionFactory.updateOrderPermission(user, order, OrderStatus.READY_FOR_DELIVERY);
		if(!permission.isPermitted()) {
			throw new RuntimeException("Permission Denied");
		}
		if(!order.getOrderStatus().equals(OrderStatus.COOKING)) {
			throw new RuntimeException("Only order COOKING status to READY_FOR_DELIVERY");
		}
		DataAccessor.changeOrderStatus(user, order, OrderStatus.READY_FOR_DELIVERY);
	}
	

}
