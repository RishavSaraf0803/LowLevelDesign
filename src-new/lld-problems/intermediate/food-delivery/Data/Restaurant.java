package FoodDeliverySystem.Data;

import java.util.List;

public class Restaurant {

	private final int restaurantId;
	private final String restaurantName;
	private final String restaurantDescription;
	private final BusinessHours businessHours;
	private final MealType mealType;
	private final List<CuisineType> cuisineType;
	private final StarRating starRating;
	private final Menu menu;
	private final Address address;
	public Restaurant(int restaurantId, String restaurantName, String restaurantDescription,
			BusinessHours businessHours, MealType mealType, List<CuisineType> cuisineType, StarRating starRating,
			Menu menu) {
		super();
		this.restaurantId = restaurantId;
		this.restaurantName = restaurantName;
		this.restaurantDescription = restaurantDescription;
		this.businessHours = businessHours;
		this.mealType = mealType;
		this.cuisineType = cuisineType;
		this.starRating = starRating;
		this.menu = menu;
		this.address = null;
	}
	public int getRestaurantId() {
		return restaurantId;
	}
	public String getRestaurantName() {
		return restaurantName;
	}
	public String getRestaurantDescription() {
		return restaurantDescription;
	}
	public BusinessHours getBusinessHours() {
		return businessHours;
	}
	public MealType getMealType() {
		return mealType;
	}
	public List<CuisineType> getCuisineType() {
		return cuisineType;
	}
	public StarRating getStarRating() {
		return starRating;
	}
	public Menu getMenu() {
		return menu;
	}
	public Address getAddress() {
		return address;
	}
	
	
	
	
}
