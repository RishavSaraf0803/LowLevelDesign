package BuilderPattern;

import java.lang.module.ModuleDescriptor.Builder;

public class User {

	private final int id;
	private final String name;
	private final String phoneNumber;
	private final int age;
	
	private User(Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.phoneNumber = builder.phoneNumber;
		this.age = builder.age;
	}
	
	
	





public static class Builder {
		

		private final int id;
		private final String name;
		private  String phoneNumber;
		private  int age;
		
		public Builder(int id, String name){
			this.id = id;
			this.name = name;
			this.phoneNumber = "";
			this.age = 0;
		}
		
		public Builder setPhoneNumber(String number) {
			this.phoneNumber = number;
			return this;
		}
		
		public Builder setAge(int age) {
			if(age < 0) {
				throw new IllegalArgumentException("Age can not be negative");
			}
			this.age = age;
			return this;
		}
		public User build() {
			return new User(this);
		}
	}
	
}

