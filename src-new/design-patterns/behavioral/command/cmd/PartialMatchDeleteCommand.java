package command.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import command.reciever.Inserter;
import command.reciever.PartialMatchDeleter;

public class PartialMatchDeleteCommand implements Command {

	
	private final Pattern regex;
	private final PartialMatchDeleter partialMatchDeleter;
	private final Inserter inserter;
	private final List<String> deletedTags;
	
	public PartialMatchDeleteCommand(Pattern regex, PartialMatchDeleter partialMatchDeleter, Inserter inserter) {
		super();
		this.regex = regex;
		this.partialMatchDeleter = partialMatchDeleter;
		this.inserter = inserter;
		this.deletedTags = new ArrayList<>();
	}

	public void execute() {
		// TODO Auto-generated method stub
		this.deletedTags.addAll(partialMatchDeleter.delete(regex));
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
