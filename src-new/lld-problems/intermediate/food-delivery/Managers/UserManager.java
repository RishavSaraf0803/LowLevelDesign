package FoodDeliverySystem.Managers;

import FoodDeliverySystem.Data.User;
import FoodDeliverySystem.DataAccessor.DataAccessObjectConverter;
import FoodDeliverySystem.DataAccessor.DataAccessResult;
import FoodDeliverySystem.DataAccessor.DataAccessor;

public class UserManager {
	
	
	public User getUserById(int userId) {
		DataAccessResult dataAccessResult = new DataAccessor().getUserById(userId);
		
		return  DataAccessObjectConverter.convertToUser(dataAccessResult);
	}
	public User getUserByToken(String token) {
		return null;
	}

}
