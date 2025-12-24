package command.reciever;

import java.util.List;

import command.database.Store;

public class Updater {

	
	public void update(String oldTag, String newTag) {
		List<String> tags = Store.getTags();
		
		for(String tag: tags) {
			if(tag.equals(oldTag)) {
				tag.replace(oldTag, newTag);
			}
		}
		return;
	}
}
