package SingletonPattern;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EmployeeStore {
	
	// Thread-safe singleton using volatile and double-checked locking
	private static volatile EmployeeStore INSTANCE = null;
	private final Map<String, String> employees;
	
	private EmployeeStore() {
		// Use ConcurrentHashMap for thread-safe operations
		this.employees = new ConcurrentHashMap<>();
		this.employees.put("101", "Bharat");
		this.employees.put("102", "Vivek");
		this.employees.put("103", "Anoop");
	}
	
	// Thread-safe singleton implementation
	public static EmployeeStore getInstanceOfEmployeeStore() {
		if(INSTANCE == null) {
			synchronized (EmployeeStore.class) {
				if(INSTANCE == null) {
					INSTANCE = new EmployeeStore();
				}
			}
		}
		return INSTANCE;
	}
	
	// Alternative: Eager initialization (thread-safe by default)
	public static EagerEmployeeStore getEagerInstance() {
		return EagerEmployeeStore.getInstance();
	}
	
	// Alternative: Bill Pugh Singleton (most recommended)
	public static BillPughEmployeeStore getBillPughInstance() {
		return BillPughEmployeeStore.getInstance();
	}
	
	public String getName(String id) {
		if(!this.employees.containsKey(id)) {
			throw new IllegalArgumentException("Id is not present");
		}
		return this.employees.get(id);
	}
	
	public void addEmployee(String id, String name) {
		this.employees.put(id, name);
	}
	
	public boolean removeEmployee(String id) {
		return this.employees.remove(id) != null;
	}
}

// Eager Initialization Singleton
class EagerEmployeeStore {
	private static final EagerEmployeeStore INSTANCE = new EagerEmployeeStore();
	private final Map<String, String> employees;
	
	private EagerEmployeeStore() {
		this.employees = new ConcurrentHashMap<>();
		this.employees.put("201", "Alice");
		this.employees.put("202", "Bob");
		this.employees.put("203", "Charlie");
	}
	
	public static EagerEmployeeStore getInstance() {
		return INSTANCE;
	}
	
	public String getName(String id) {
		return this.employees.get(id);
	}
}

// Bill Pugh Singleton (Most Recommended)
class BillPughEmployeeStore {
	private final Map<String, String> employees;
	
	private BillPughEmployeeStore() {
		this.employees = new ConcurrentHashMap<>();
		this.employees.put("301", "David");
		this.employees.put("302", "Eve");
		this.employees.put("303", "Frank");
	}
	
	// Inner static class - thread-safe lazy initialization
	private static class SingletonHelper {
		private static final BillPughEmployeeStore INSTANCE = new BillPughEmployeeStore();
	}
	
	public static BillPughEmployeeStore getInstance() {
		return SingletonHelper.INSTANCE;
	}
	
	public String getName(String id) {
		return this.employees.get(id);
	}
}
