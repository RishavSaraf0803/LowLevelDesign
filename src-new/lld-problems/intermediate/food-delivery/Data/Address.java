package FoodDeliverySystem.Data;

public class Address {

	private final String addressLine1;

	private final String addressLine2;

	private final String addressLine3;
	private final String city;
	private final String State;
	private final String zip;
	private final String country;
	public Address(String addressLine1, String addressLine2, String addressLine3, String city, String state, String zip,
			String country) {
		super();
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.addressLine3 = addressLine3;
		this.city = city;
		State = state;
		this.zip = zip;
		this.country = country;
	}
	public String getAddressLine1() {
		return addressLine1;
	}
	public String getAddressLine2() {
		return addressLine2;
	}
	public String getAddressLine3() {
		return addressLine3;
	}
	public String getCity() {
		return city;
	}
	public String getState() {
		return State;
	}
	public String getZip() {
		return zip;
	}
	public String getCountry() {
		return country;
	}
	
}
