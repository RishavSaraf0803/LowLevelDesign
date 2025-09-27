package command.reciever;

import java.util.ArrayList;
import java.util.List;

import command.database.Store;

public class PerfectMatchDeleter {

	
	public List<String> delete(String name) {
		List<String> tags = Store.getTags();
		List<String> deletedTags = new ArrayList<>();
		for(String tag : tags) {
			if(tag.equals(name)) {
				Store.delete(tag);
				deletedTags.add(tag);
				
			}
		}
		
		return deletedTags;
	}
}
