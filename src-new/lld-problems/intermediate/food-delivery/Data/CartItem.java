package FoodDeliverySystem.Data;

public class CartItem {
	
	private final FoodItem foodItem;
	private final int quantity;
	public CartItem(FoodItem foodItem, int quantity) {
		super();
		this.foodItem = foodItem;
		this.quantity = quantity;
	}
	public FoodItem getFoodItem() {
		return foodItem;
	}
	public int getQuantity() {
		return quantity;
	}
	

}
