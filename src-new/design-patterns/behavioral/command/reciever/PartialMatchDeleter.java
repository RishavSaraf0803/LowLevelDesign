package command.reciever;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import command.database.Store;

public class PartialMatchDeleter {

	
	public List<String> delete(Pattern pattern) {
		List<String> tags  = new ArrayList<>();
		List<String> deletedTags = new ArrayList<>();
		Store.getTags().forEach(tag -> tags.add(tag));
		for(String tag : tags) {
			if(pattern.matcher(tag).matches()) { 
				//System.out.println("Deleted tags are" + " " + tag);
				Store.delete(tag);
				deletedTags.add(tag);
			}
		}
		return deletedTags;
	}
}
