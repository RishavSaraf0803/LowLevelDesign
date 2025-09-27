package FoodDeliverySystem.Data;

import java.time.LocalDate;

public class PaymentResponse {
	
	private final int paymentId;
	private final double amount;
	
	private final PaymentStatus paymentStatus;

	public PaymentResponse(int paymentId, double amount, PaymentStatus paymentStatus) {
		super();
		this.paymentId = paymentId;
		this.amount = amount;
		this.paymentStatus = paymentStatus;
	}

	public int getPaymentId() {
		return paymentId;
	}

	public double getAmount() {
		return amount;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}
	
	

}
