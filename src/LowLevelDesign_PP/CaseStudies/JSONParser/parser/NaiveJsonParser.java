package JSONParser.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import JSONParser.data.JSON;
import JSONParser.data.KeyValuePair;
import JSONParser.data.StringConstants;
import JSONParser.tokenizer.Tokenizer;

public class NaiveJsonParser implements JsonParser {

	private final Tokenizer tokenizer;
	
	
	public NaiveJsonParser(Tokenizer tokenizer) {
		super();
		this.tokenizer = tokenizer;
	}

	@Override
	public JSON parse(String jsonText) {
		// TODO Auto-generated method stub
		if(jsonText == null) {
			throw new RuntimeException("Invalid jsonText");
			
		}
		if(!jsonText.contains(StringConstants.STARTING_PARAN_STRING)) {
			jsonText = jsonText.trim().replaceAll(StringConstants.DOUBLE_QUOTES_STRING,StringConstants.EMPTY_STRING);
			
			Map<String,JSON> keyToValue = new HashMap<>();
			keyToValue.put(jsonText, null);
			return new JSON(keyToValue);
		}
		
		List<KeyValuePair> keyValuePairs = this.tokenizer.tokenize(jsonText);
		
		Map<String, JSON> keyToValues = new HashMap<>();
		for(KeyValuePair keyValuePair : keyValuePairs) {
			keyToValues.put(keyValuePair.getKey(), parse(keyValuePair.getValue()));
		}
		
		return new JSON(keyToValues);
	}

	@Override
	public String toString(JSON json) {
		// TODO Auto-generated method stub
		String jsonText = StringConstants.STARTING_PARAN_STRING;
		List<String> jsonKeys = json.getAllKeys();
		for(String key: jsonKeys) {
			jsonText += StringConstants.DOUBLE_QUOTES + key + StringConstants.DOUBLE_QUOTES;
			jsonText += StringConstants.COLON;
			jsonText += toString(json.get(key));
			jsonText += StringConstants.COMMA;
		}
		if(jsonText.endsWith(StringConstants.COMMA_STRING)) {
			jsonText = jsonText.substring(0, jsonText.length()-1);
		}
		jsonText += StringConstants.ENDING_PARAN_STRING;
		return jsonText;
	}

}
