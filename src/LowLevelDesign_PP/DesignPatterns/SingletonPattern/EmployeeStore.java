package SingletonPattern;

import java.util.HashMap;
import java.util.Map;

public class EmployeeStore {
	
	private  static EmployeeStore INSTANCE = null;
	private final Map<String, String> employees;
	private EmployeeStore() {
		this.employees = new HashMap<>();
		this.employees.put("101", "Bharat");

		this.employees.put("101", "Vivek");

		this.employees.put("101", "Anoop");
	}
	
	public EmployeeStore getInstanceOfEmployeeStore() {
		if(INSTANCE == null) {
			INSTANCE = new EmployeeStore();
		}
		return INSTANCE;
	}
	
	public String getName(String id) {
		if(!this.employees.containsKey(id)) {
			throw new IllegalArgumentException("Id is not present");
		}
		return this.employees.get(id);
	}
}
