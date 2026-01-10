package FoodDeliverySystem.Managers;

import FoodDeliverySystem.Data.PaymentResponse;

public class NetBankingPaymentManager implements PaymentManager {

	private final String bankName;
	private final String userName;
	private final String passWord;
	private final String pin;
	private final double amount;
	
	
	public NetBankingPaymentManager(String bankName, String userName, String passWord, String pin, double amount) {
		super();
		this.bankName = bankName;
		this.userName = userName;
		this.passWord = passWord;
		this.pin = pin;
		this.amount = amount;
	}


	public PaymentResponse executePayment() {
		return null;
	}


	public String getBankName() {
		return bankName;
	}


	public String getUserName() {
		return userName;
	}


	public String getPassWord() {
		return passWord;
	}


	public String getPin() {
		return pin;
	}


	public double getAmount() {
		return amount;
	}
	
	
	
}
