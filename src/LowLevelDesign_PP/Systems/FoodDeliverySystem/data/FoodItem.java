package FoodDeliverySystem.Data;

import java.lang.module.ModuleDescriptor.Builder;

/**
 * @author rishavsaraf
 *
 */
public class FoodItem {
	
	private final int id;
	private final String itemName;
	private final String description;
	private final double priceINR;
	private final CuisineType cusineType;
	private final MealType mealType;
	private final StarRating starRating;
	private final int restaurantId;
	private final boolean isAvailable;
	
	public FoodItem(Builder builder) {
		this.id = builder.id;
		this.itemName = builder.itemName;
		this.description = builder.description;
		this.priceINR = builder.priceINR;
		this.cusineType = builder.cusineType;
		this.mealType = builder.mealType;
		this.starRating = builder.starRating;
		this.restaurantId =  builder.restaurantId;
		this.isAvailable = builder.isAvailable;
	}
	
	 
	
	public int getId() {
		return id;
	}



	public String getItemName() {
		return itemName;
	}



	public String getDescription() {
		return description;
	}



	public double getPriceINR() {
		return priceINR;
	}



	public CuisineType getCusineType() {
		return cusineType;
	}



	public MealType getMealType() {
		return mealType;
	}



	public StarRating getStarRating() {
		return starRating;
	}



	public int getRestaurantId() {
		return restaurantId;
	}



	public boolean isAvailable() {
		return isAvailable;
	}



	public static class Builder {
		
		private final int id;
		private final String itemName;
		private final String description;
		private double priceINR;
		private final CuisineType cusineType;
		private final MealType mealType;
		private StarRating starRating;
		private int restaurantId;
		private boolean isAvailable;
		public Builder(int id, String itemName, String description, CuisineType cusineType, MealType mealType) {
			super();
			this.id = id;
			this.itemName = itemName;
			this.description = description;
			this.cusineType = cusineType;
			this.mealType = mealType;
		}
		
		public void setPriceINR(double priceINR) {
			if(priceINR < 0)throw new IllegalArgumentException("Price Can not be NEGATIVE");
			this.priceINR = priceINR;
		}
		
		public void setStarRating(StarRating starRating) {
			this.starRating = starRating;
		}
		
		public void setRestaurantId(int restaurantId) {
			this.restaurantId = restaurantId;
		}
		
		public void setAvailable(boolean isAvailable) {
			this.isAvailable = isAvailable;
		}
		
		
		
		
		
		
		
	}

}
