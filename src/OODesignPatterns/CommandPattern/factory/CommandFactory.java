package command.factory;

import java.util.regex.Pattern;

import command.cmd.Command;
import command.cmd.PartialMatchDeleteCommand;
import command.cmd.PerfectMatchDeleteComand;
import command.reciever.Inserter;
import command.reciever.PartialMatchDeleter;
import command.reciever.PerfectMatchDeleter;

public class CommandFactory {

	
	
	private CommandFactory() {
		
	}
	public static Command getPartialMatchCommand(Pattern pattern) {
		return new PartialMatchDeleteCommand(pattern, new PartialMatchDeleter(), new Inserter());
	}
	
	
	public static Command getPerfectMatchCommand(String name) {
		return new PerfectMatchDeleteComand(name, new PerfectMatchDeleter(), new Inserter());
	}
}
