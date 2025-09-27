package ParkingLot.Factory;

import ParkingLot.Data.CardDetail;
import ParkingLot.Payment.CardPaymentProcessor;
import ParkingLot.Payment.PaymentProcessor;

public class PaymentProcessorFactory {

	private PaymentProcessorFactory() {}
	public static PaymentProcessor getCardBasePaymentProcessor(double amount, CardDetail cardDetails) {
		return new CardPaymentProcessor(amount,cardDetails);
	}
	public static PaymentProcessor getCashBasePaymentProcessor(double amount) {
		return new CashBasePaymentProcessor(amount);
	}
}
