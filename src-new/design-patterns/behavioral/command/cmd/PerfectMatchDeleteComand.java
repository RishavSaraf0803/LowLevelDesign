package command.cmd;

import java.util.ArrayList;
import java.util.List;

import command.database.Store;
import command.reciever.Inserter;
import command.reciever.PerfectMatchDeleter;

public class PerfectMatchDeleteComand implements Command {

	private final String name;
	private final PerfectMatchDeleter perfectMatchDeleter;
	private final Inserter inserter;
	private final List<String> deletedTags;
	// reciever
	public PerfectMatchDeleteComand(String name, PerfectMatchDeleter perfectMatchDeleter,Inserter inserter) {
		super();
		this.name = name;
		this.perfectMatchDeleter = perfectMatchDeleter;
		this.inserter = inserter;
		deletedTags = new ArrayList<>();
		
	}


	public void execute() {
		// TODO Auto-generated method stub
		this.deletedTags.addAll(perfectMatchDeleter.delete(this.name));
		
		return;
	}


	@Override
	public void undo() {
		// TODO Auto-generated method stub
		for(String tag : deletedTags) {
			inserter.insert(tag);
		}
		deletedTags.clear();
	}

}
