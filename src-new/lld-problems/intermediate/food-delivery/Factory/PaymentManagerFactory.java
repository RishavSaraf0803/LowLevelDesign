package FoodDeliverySystem.Factory;

import java.util.Map;

import FoodDeliverySystem.Data.PaymentMode;
import FoodDeliverySystem.Managers.DebitCardPaymentManager;
import FoodDeliverySystem.Managers.NetBankingPaymentManager;
import FoodDeliverySystem.Managers.PaymentManager;

public class PaymentManagerFactory {

	private PaymentManagerFactory() {
		
	}
	
	public static PaymentManager getPaymentManager(Map<String,String> paymentInfo, PaymentMode paymentMode) throws Exception {
		if(paymentMode.equals("NETBANKING")) {
			return getNetBankingPaymentManager(paymentInfo);
		}
		
		else if(paymentMode.equals("DEBITCARD")) {
			return getDebitCardPaymentManager(paymentInfo);
		}
		else if(paymentMode.equals("CREDITCARD")) {
			return getCreditCardPaymentManager(paymentInfo);
		}
		throw new RuntimeException("Invalid Payment Mode");
	}
	
	private static PaymentManager getNetBankingPaymentManager(Map<String, String> paymentInfo) {
		String bankName = paymentInfo.get("bankName");
		String userName = paymentInfo.get("userName");
		String passWord = paymentInfo.get("passWord");
		String pin = paymentInfo.get("pin");
		String amount = paymentInfo.get("amount");
		double totalAmount = Double.parseDouble(amount);
		return new NetBankingPaymentManager(bankName, userName, passWord, pin, totalAmount);
		
	
		
	}
	
	private static PaymentManager getDebitCardPaymentManager(Map<String, String> paymentInfo) {
		String bankName = paymentInfo.get("bankName");
		String userName = paymentInfo.get("cardNumber");
		String pin = paymentInfo.get("pin");
		String amount = paymentInfo.get("amount");
		double totalAmount = Double.parseDouble(amount);
		return new DebitCardPaymentManager(bankName, userName,pin, totalAmount);
		
	}
	
	private static PaymentManager getCreditCardPaymentManager(Map<String, String> paymentInfo) {
		return null;
		
	}
}
