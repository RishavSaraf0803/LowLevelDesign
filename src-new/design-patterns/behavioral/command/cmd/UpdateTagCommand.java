package command.cmd;

import command.reciever.Inserter;
import command.reciever.PerfectMatchDeleter;
import command.reciever.Updater;

public class UpdateTagCommand implements Command {

	
	private final String oldTag;
	private final String newTag;
	private final Updater updater;
	private final Inserter inserter;
	private final PerfectMatchDeleter perfectMatchDeleter;
	
	public UpdateTagCommand(String oldTag, String newTag, Updater updater,Inserter inserter, PerfectMatchDeleter perfectMatchDeleter) {
		super();
		this.oldTag = oldTag;
		this.newTag = newTag;
		this.updater = updater;
		this.inserter = inserter;
		this.perfectMatchDeleter = perfectMatchDeleter;
	}


	@Override
	public void execute() {
		// TODO Auto-generated method stub
         updater.update(oldTag, newTag);
	}


	@Override
	public void undo() {
		// TODO Auto-generated method stub
		perfectMatchDeleter.delete(newTag);
		inserter.insert(oldTag);
	}

}
