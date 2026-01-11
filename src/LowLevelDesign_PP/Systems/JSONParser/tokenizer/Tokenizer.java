package JSONParser.tokenizer;

import java.util.List;

import JSONParser.data.KeyValuePair;

public interface Tokenizer {
	
	List<KeyValuePair> tokenize(String text);

}
