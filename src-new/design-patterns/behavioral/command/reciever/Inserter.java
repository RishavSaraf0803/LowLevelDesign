package command.reciever;

import command.database.Store;

public class Inserter {

	public void insert(String tag) {
		Store.insert(tag);
		return;
	}
}
