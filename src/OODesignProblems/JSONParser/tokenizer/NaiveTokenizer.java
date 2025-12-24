package JSONParser.tokenizer;

import java.util.ArrayList;
import java.util.List;

import JSONParser.data.KeyValuePair;
import JSONParser.data.StringConstants;

public class NaiveTokenizer implements Tokenizer {

	@Override
	public List<KeyValuePair> tokenize(String text) {
		// TODO Auto-generated method stub
		
		String jsonText = preProcess(text);
		List<String> keyValuePairs = new ArrayList<>();
		int startIndx = 0, endIndx;

		while(text.length() > 0) {
			endIndx = getEndIndex(text);
			keyValuePairs.add(text.substring(startIndx, endIndx));
			if(endIndx >= text.length())break;
			
			text = text.substring(endIndx+1);
		}
		List<KeyValuePair> keyValTokens = new ArrayList<>();
		for(String keyValPair : keyValuePairs) {
			int idx = keyValPair.indexOf(StringConstants.COLON);
			String key = keyValPair.substring(0, idx).trim().replaceAll(StringConstants.DOUBLE_QUOTES_STRING, StringConstants.EMPTY);
			String val = keyValPair.substring(idx+1);
			keyValTokens.add(new KeyValuePair(key,val));
		}
		return keyValTokens;
		
	}
	private String preProcess(String jsonText) {
		try {
		jsonText = jsonText.trim();
		jsonText = jsonText.substring(1, jsonText.length()-1);
		jsonText = jsonText.trim();
		return jsonText;
		}
		catch(Exception e) {
			throw new RuntimeException("Invalid text");
		} 
	}
	
	private int getEndIndex(String text) {
		int index = text.indexOf(StringConstants.COLON);
		int endIdx = index+1;
		while(text.charAt(endIdx)== StringConstants.WHITESPACE) {
			endIdx++;
		}
		if(text.charAt(endIdx) == StringConstants.DOUBLE_QUOTES) {
			endIdx++;
			while(endIdx < text.length() && text.charAt(endIdx) != StringConstants.DOUBLE_QUOTES) {
				endIdx++;
		}
			
		endIdx++;
		}
		else if(text.charAt(endIdx) == StringConstants.STARTING_PARAN) {
			int cnt = 1;
			endIdx++;
			while(cnt != 0 && text.length() > endIdx) {
				if(text.charAt(endIdx) == StringConstants.CLOSING_PARAN)cnt--;
				else if(text.charAt(endIdx) == StringConstants.STARTING_PARAN)cnt++;
				endIdx++;
			}
		}
		else {
			throw new RuntimeException("Illegal json");
		}
		while(endIdx < text.length() && text.charAt(endIdx) != StringConstants.COMMA) {
			endIdx++;
		}
		return endIdx;
	}

}
