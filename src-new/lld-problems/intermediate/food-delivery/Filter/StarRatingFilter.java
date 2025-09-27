package FoodDeliverySystem.Filter;

import FoodDeliverySystem.Data.FoodItem;
import FoodDeliverySystem.Data.Restaurant;
import FoodDeliverySystem.Data.StarRating;

public class StarRatingFilter implements FoodItemFilter, RestaurantFilter {

	private final StarRating rating;
	
	public StarRatingFilter(StarRating rating) {
		super();
		this.rating = rating;
	}

	@Override
	public boolean filter(FoodItem foodItem) {
		// TODO Auto-generated method stub
		return foodItem.getStarRating().getVal() == rating.getVal();
	}

	@Override
	public boolean filter(Restaurant restaurant) {
		// TODO Auto-generated method stub
		return restaurant.getStarRating().getVal() == rating.getVal();
	}

}
