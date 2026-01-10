package command.cmd;

import command.reciever.Inserter;
import command.reciever.PerfectMatchDeleter;

public class InsertTagCommand implements Command {
	
	private final String name;
	private final Inserter inserter;
	
	private final PerfectMatchDeleter perfectMatchDeleter;
	

	public InsertTagCommand(String name, Inserter inserter, PerfectMatchDeleter perfectMatchDeleter) {
		super();
		this.name = name;
		this.inserter = inserter;
		this.perfectMatchDeleter = perfectMatchDeleter;
	}



	@Override
	public void execute() {
		// TODO Auto-generated method stub
		inserter.insert(name);
	}



	@Override
	public void undo() {
		// TODO Auto-generated method stub
		perfectMatchDeleter.delete(name);
	}

}
