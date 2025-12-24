package ParkingLot.Payment;

import ParkingLot.Data.CardDetail;
import ParkingLot.Data.PaymentResponse;

public class CardPaymentProcessor implements PaymentProcessor {

	private final double amount;
	private final CardDetail cardDetails;
	
	
	public CardPaymentProcessor(double amount, CardDetail cardDetails) {
		super();
		this.amount = amount;
		this.cardDetails = cardDetails;
	}


	@Override
	public PaymentResponse executePayment() {
		// TODO Auto-generated method stub
		return null;
	}


	

}
