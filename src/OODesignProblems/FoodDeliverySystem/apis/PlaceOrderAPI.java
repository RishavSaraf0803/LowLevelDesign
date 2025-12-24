package FoodDeliverySystem.apis;

import java.util.List;
import java.util.Map;

import FoodDeliverySystem.Data.CartItem;
import FoodDeliverySystem.Data.Order;
import FoodDeliverySystem.Data.PaymentMode;
import FoodDeliverySystem.Data.PaymentResponse;
import FoodDeliverySystem.Data.PaymentStatus;
import FoodDeliverySystem.Data.User;
import FoodDeliverySystem.Factory.PaymentManagerFactory;
import FoodDeliverySystem.Managers.CartManager;
import FoodDeliverySystem.Managers.OrderManager;
import FoodDeliverySystem.Managers.PaymentManager;
import FoodDeliverySystem.Managers.UserManager;

public class PlaceOrderAPI {
	private final UserManager userManager = new UserManager();
	private final CartManager cartManager = new CartManager();
	private final OrderManager orderManager = new OrderManager();
	
	public Order placeOrder(String userToken, Map<String, String> paymentInfo, PaymentMode paymentMode) throws Exception {
		
		if(userToken == null || userToken.length() == 0) {
			throw new IllegalArgumentException("Params missing");
		}
		User user = userManager.getUserByToken(userToken);
		if(user == null) {
			throw new IllegalArgumentException("User token is invalid");
		}
		
		PaymentManager paymentManager = PaymentManagerFactory.getPaymentManager(paymentInfo, paymentMode);
		PaymentResponse paymentResponse = paymentManager.executePayment();
		
		if(paymentResponse == null || paymentResponse.getPaymentStatus()== null || paymentResponse.getPaymentStatus().equals(PaymentStatus.FAILURE)) {
			throw new RuntimeException("payment Failed");
		}
		
		
		return orderManager.placeOrder(user);
		
	}
}
