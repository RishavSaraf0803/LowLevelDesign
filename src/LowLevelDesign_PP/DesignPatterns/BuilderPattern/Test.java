package BuilderPattern;

import BuilderPattern.User.Builder;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//chaining
		User user1 = new User.Builder(2,"VIVEK").
				setAge(23).setPhoneNumber("948434").build();
		
		User user2 = new User.Builder(3, "Rishav").setAge(-9).build();
		
	}

}
