package JSONParser.parser;

import JSONParser.data.JSON;

public interface JsonParser {
	
	JSON parse(String jsonText);
	
	String toString(JSON json);

}
