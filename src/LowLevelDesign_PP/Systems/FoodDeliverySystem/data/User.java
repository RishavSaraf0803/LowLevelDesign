package FoodDeliverySystem.Data;

public class User {

	private final int userId;
	private final String userName;
	private final Address userAddress;
	private final String userPhoneNumber;
	private final String userEmailId;
	public User(int userId, String userName, Address userAddress, String userPhoneNumber, String userEmailId) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userAddress = userAddress;
		this.userPhoneNumber = userPhoneNumber;
		this.userEmailId = userEmailId;
	}
	public int getUserId() {
		return userId;
	}
	public String getUserName() {
		return userName;
	}
	public Address getUserAddress() {
		return userAddress;
	}
	public String getUserPhoneNumber() {
		return userPhoneNumber;
	}
	public String getUserEmailId() {
		return userEmailId;
	}
	
	
}

