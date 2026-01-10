package JSONParser.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JSON {

	private final Map<String, JSON>  keyToValue;

	public JSON(Map<String, JSON> keyToValue) {
		super();
		this.keyToValue = keyToValue;
	}
	
	public JSON get(String key) {
		return this.keyToValue.get(key);
	}
	 public List<String> getAllKeys(){
		 List<String> keys = new ArrayList<>();
		 for(Map.Entry<String, JSON> entry: keyToValue.entrySet()) {
			 keys.add(entry.getKey());
		 }
		 return keys;
	 }
}
