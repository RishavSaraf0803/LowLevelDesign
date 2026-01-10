package command;

import java.util.List;
import java.util.Stack;

import command.cmd.Command;

public class TagManager {

	
	private final Stack<Command> oldCommands;
	
	public TagManager() {
		super();
		//this.command = command;
		oldCommands = new Stack<>();
	}


	public void changeTags(Command command) {
		command.execute();
		oldCommands.add(command);
	}
	public void undoChangeTags() {
		if(oldCommands.isEmpty()) {
			throw new RuntimeException("No Commands for undo");
		}
		Command command = oldCommands.peek();
		command.undo();
		oldCommands.pop();
	}

}
