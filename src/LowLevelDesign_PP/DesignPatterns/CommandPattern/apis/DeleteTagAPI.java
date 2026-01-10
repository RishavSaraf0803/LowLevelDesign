package command.apis;

import java.util.regex.Pattern;

import command.TagManager;
import command.cmd.Command;
import command.cmd.PerfectMatchDeleteComand;
import command.data.MatchType;
import command.factory.CommandFactory;
import command.reciever.PartialMatchDeleter;
import command.reciever.PerfectMatchDeleter;

public class DeleteTagAPI {

	
	public void deleteTags(String name, MatchType matchType) {
		Command command = null;
		if(matchType.equals(MatchType.PERFECT)) {
			command = CommandFactory.getPerfectMatchCommand(name);
		}
		else if(matchType.equals(MatchType.PARTIAL)) {
			command = CommandFactory.getPartialMatchCommand(Pattern.compile(name));
		}
		else {
			throw new IllegalArgumentException("Illegal Argument");
		}
		
		TagManager tagManager = new TagManager();
		tagManager.changeTags(command);
	}
}
