package FoodDeliverySystem.Managers;

import FoodDeliverySystem.Data.PaymentResponse;

public class DebitCardPaymentManager implements PaymentManager{
	
	private final String bankName;
	private final String cardNumber;
	private final String pin;
	private final double amount;
	
	
	
	public DebitCardPaymentManager(String bankName, String cardNumber, String pin, double amount) {
		super();
		this.bankName = bankName;
		this.cardNumber = cardNumber;
		this.pin = pin;
		this.amount = amount;
	}

	
	@Override
	public PaymentResponse executePayment() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getBankName() {
		return bankName;
	}




	public String getCardNumber() {
		return cardNumber;
	}




	public String getPin() {
		return pin;
	}




	public double getAmount() {
		return amount;
	}




	
	

}
