package ParkingLot.Data;

public class CardDetail {
	
	private final String cardNumber;
	private final String nameOnCard;
	private final int pin;
	public CardDetail(String cardNumber, String nameOnCard, int pin) {
		super();
		this.cardNumber = cardNumber;
		this.nameOnCard = nameOnCard;
		this.pin = pin;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public String getNameOnCard() {
		return nameOnCard;
	}
	public int getPin() {
		return pin;
	}
	

}
